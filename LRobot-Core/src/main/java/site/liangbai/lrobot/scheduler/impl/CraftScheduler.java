package site.liangbai.lrobot.scheduler.impl;

import org.apache.commons.lang3.Validate;
import site.liangbai.lrobot.plugin.Plugin;
import site.liangbai.lrobot.plugin.exception.InvalidPluginException;
import site.liangbai.lrobot.scheduler.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The fundamental concepts for this implementation:
 * <li>Main thread owns {@link #head} and {@link #currentTick}, but it may be read from any thread</li>
 * <li>Main thread exclusively controls {@link #temp} and {@link #pending}.
 *     They are never to be accessed outside of the main thread; alternatives exist to prevent locking.</li>
 * <li>{@link #head} to {@link #tail} act as a linked list/queue, with 1 consumer and infinite producers.
 *     Adding to the tail is atomic and very efficient; utility method is {@link #handle(CraftTask, long)} or {@link #addTask(CraftTask)}. </li>
 * <li>Changing the period on a task is delicate.
 *     Any future task needs to notify waiting threads.
 *     Async tasks must be synchronized to make sure that any thread that's finishing will remove itself from {@link #runners}.
 * <li>{@link #runners} provides a moderately up-to-date view of active tasks.
 *     If the linked head to tail set is read, all remaining tasks that were active at the time execution started will be located in runners.</li>
 * <li>Async tasks are responsible for removing themselves from runners</li>
 * <li>Sync tasks are only to be removed from runners on the main thread when coupled with a removal from pending and temp.</li>
 * <li>Most of the design in this scheduler relies on queuing special tasks to perform any data changes on the main thread.
 *     When executed from inside a synchronous method, the scheduler will be updated before next execution by virtue of the frequent {@link #parsePending()} calls.</li>
 */
public class CraftScheduler implements LScheduler {

    /**
     * Counter for IDs. Order doesn't matter, only uniqueness.
     */
    private final AtomicInteger ids = new AtomicInteger(1);

    private volatile CraftTask head = new CraftTask();
    /**
     * Tail of a linked-list. AtomicReference only matters when adding to queue
     */
    private final AtomicReference<CraftTask> tail = new AtomicReference<>(head);
    /**
     * Main thread logic only
     */
    private final PriorityQueue<CraftTask> pending = new PriorityQueue<>(10,
            (o1, o2) -> (int) (o1.getNextRun() - o2.getNextRun()));
    /**
     * Main thread logic only
     */
    private final List<CraftTask> temp = new ArrayList<>();
    /**
     * These are tasks that are currently active. It's provided for 'viewing' the current state.
     */
    private final ConcurrentHashMap<Integer, CraftTask> runners = new ConcurrentHashMap<>();
    private volatile int currentTick = -1;
    private final Executor executor = Executors.newCachedThreadPool();
    private CraftAsyncDebugger debugHead = new CraftAsyncDebugger(-1, null, null) {@Override StringBuilder debugTo(StringBuilder string) {return string;}};
    private CraftAsyncDebugger debugTail = debugHead;
    private static final int RECENT_TICKS;

    static {
        RECENT_TICKS = 30;
    }

    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleSyncDelayedTask(plugin, task, 0);
    }

    public LTask runTask(Plugin plugin, Runnable runnable) {
        return runTaskLater(plugin, runnable, 0);
    }

    @Deprecated
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task) {
        return this.scheduleAsyncDelayedTask(plugin, task, 0);
    }

    public LTask runTaskAsynchronously(Plugin plugin, Runnable runnable) {
        return runTaskLaterAsynchronously(plugin, runnable, 0);
    }

    public int scheduleSyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleSyncRepeatingTask(plugin, task, delay, -1);
    }

    public LTask runTaskLater(Plugin plugin, Runnable runnable, long delay) {
        return runTaskTimer(plugin, runnable, delay, -1);
    }

    @Deprecated
    public int scheduleAsyncDelayedTask(final Plugin plugin, final Runnable task, final long delay) {
        return this.scheduleAsyncRepeatingTask(plugin, task, delay, -1);
    }

    public LTask runTaskLaterAsynchronously(Plugin plugin, Runnable runnable, long delay) {
        return runTaskTimerAsynchronously(plugin, runnable, delay, -1);
    }

    public int scheduleSyncRepeatingTask(final Plugin plugin, final Runnable runnable, long delay, long period) {
        return runTaskTimer(plugin, runnable, delay, period).getTaskId();
    }

    public LTask runTaskTimer(Plugin plugin, Runnable runnable, long delay, long period) {
        validate(plugin, runnable);
        if (delay < 0) {
            delay = 0;
        }
        if (period == 0) {
            period = 1;
        } else if (period < -1) {
            period = -1;
        }
        return handle(new CraftTask(plugin, runnable, nextId(), period), delay);
    }

    @Deprecated
    public int scheduleAsyncRepeatingTask(final Plugin plugin, final Runnable runnable, long delay, long period) {
        return runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
    }

    public LTask runTaskTimerAsynchronously(Plugin plugin, Runnable runnable, long delay, long period) {
        validate(plugin, runnable);
        if (delay < 0) {
            delay = 0;
        }
        if (period == 0) {
            period = 1;
        } else if (period < -1) {
            period = -1;
        }
        return handle(new CraftAsyncTask(runners, plugin, runnable, nextId(), period), delay);
    }

    public <T> Future<T> callSyncMethod(final Plugin plugin, final Callable<T> task) {
        validate(plugin, task);
        final CraftFuture<T> future = new CraftFuture<>(task, plugin, nextId());
        handle(future, 0);
        return future;
    }

    public void cancelTask(final int taskId) {
        if (taskId <= 0) {
            return;
        }
        CraftTask task = runners.get(taskId);
        if (task != null) {
            task.cancel0();
        }
        task = new CraftTask(
                new Runnable() {
                    public void run() {
                        if (!check(CraftScheduler.this.temp)) {
                            check(CraftScheduler.this.pending);
                        }
                    }
                    private boolean check(final Iterable<CraftTask> collection) {
                        final Iterator<CraftTask> tasks = collection.iterator();
                        while (tasks.hasNext()) {
                            final CraftTask task = tasks.next();
                            if (task.getTaskId() == taskId) {
                                task.cancel0();
                                tasks.remove();
                                if (task.isSync()) {
                                    runners.remove(taskId);
                                }
                                return true;
                            }
                        }
                        return false;
                    }});
        handle(task, 0);
        for (CraftTask taskPending = head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() == taskId) {
                taskPending.cancel0();
            }
        }
    }

    public void cancelTasks(final Plugin plugin) {
        Validate.notNull(plugin, "Cannot cancel tasks of null plugin");
        final CraftTask task = new CraftTask(
                new Runnable() {
                    public void run() {
                        check(CraftScheduler.this.pending);
                        check(CraftScheduler.this.temp);
                    }
                    void check(final Iterable<CraftTask> collection) {
                        final Iterator<CraftTask> tasks = collection.iterator();
                        while (tasks.hasNext()) {
                            final CraftTask task = tasks.next();
                            if (task.getOwner().equals(plugin)) {
                                task.cancel0();
                                tasks.remove();
                                if (task.isSync()) {
                                    runners.remove(task.getTaskId());
                                }
                            }
                        }
                    }
                });
        handle(task, 0);
        for (CraftTask taskPending = head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() != -1 && taskPending.getOwner().equals(plugin)) {
                taskPending.cancel0();
            }
        }
        for (CraftTask runner : runners.values()) {
            if (runner.getOwner().equals(plugin)) {
                runner.cancel0();
            }
        }
    }

    public void cancelAllTasks() {
        final CraftTask task = new CraftTask(
                () -> {
                    Iterator<CraftTask> it = CraftScheduler.this.runners.values().iterator();
                    while (it.hasNext()) {
                        CraftTask task1 = it.next();
                        task1.cancel0();
                        if (task1.isSync()) {
                            it.remove();
                        }
                    }
                    CraftScheduler.this.pending.clear();
                    CraftScheduler.this.temp.clear();
                });
        handle(task, 0);
        for (CraftTask taskPending = head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                break;
            }
            taskPending.cancel0();
        }
        for (CraftTask runner : runners.values()) {
            runner.cancel0();
        }
    }

    public boolean isCurrentlyRunning(final int taskId) {
        final CraftTask task = runners.get(taskId);
        if (task == null || task.isSync()) {
            return false;
        }
        final CraftAsyncTask asyncTask = (CraftAsyncTask) task;
        synchronized (asyncTask.getWorkers()) {
            return asyncTask.getWorkers().isEmpty();
        }
    }

    public boolean isQueued(final int taskId) {
        if (taskId <= 0) {
            return false;
        }
        for (CraftTask task = head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() == taskId) {
                return task.getPeriod() >= -1; // The task will run
            }
        }
        CraftTask task = runners.get(taskId);
        return task != null && task.getPeriod() >= -1;
    }

    public List<LWorker> getActiveWorkers() {
        final ArrayList<LWorker> workers = new ArrayList<>();
        for (final CraftTask taskObj : runners.values()) {
            // Iterator will be a best-effort (may fail to grab very new values) if called from an async thread
            if (taskObj.isSync()) {
                continue;
            }
            final CraftAsyncTask task = (CraftAsyncTask) taskObj;
            synchronized (task.getWorkers()) {
                // This will never have an issue with stale threads; it's state-safe
                workers.addAll(task.getWorkers());
            }
        }
        return workers;
    }

    public List<LTask> getPendingTasks() {
        final ArrayList<CraftTask> truePending = new ArrayList<>();
        for (CraftTask task = head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() != -1) {
                // -1 is special code
                truePending.add(task);
            }
        }

        final ArrayList<LTask> pending = new ArrayList<>();
        for (CraftTask task : runners.values()) {
            if (task.getPeriod() >= -1) {
                pending.add(task);
            }
        }

        for (final CraftTask task : truePending) {
            if (task.getPeriod() >= -1 && !pending.contains(task)) {
                pending.add(task);
            }
        }
        return pending;
    }

    /**
     * This method is designed to never block or wait for locks; an immediate execution of all current tasks.
     */
    public void mainThreadHeartbeat(final int currentTick) {
        this.currentTick = currentTick;
        final List<CraftTask> temp = this.temp;
        parsePending();
        while (isReady(currentTick)) {
            final CraftTask task = pending.remove();
            if (task.getPeriod() < -1) {
                if (task.isSync()) {
                    runners.remove(task.getTaskId(), task);
                }
                parsePending();
                continue;
            }
            if (task.isSync()) {
                try {
                    task.run();
                } catch (final Throwable throwable) {
                    task.getOwner().getLogger().error(
                            String.format(
                                "Task #%s for %s generated an exception",
                                task.getTaskId(),
                                task.getOwner().getDescription().getName()),
                            throwable);
                }
                parsePending();
            } else {
                debugTail = debugTail.setNext(new CraftAsyncDebugger(currentTick + RECENT_TICKS, task.getOwner(), task.getTaskClass()));
                executor.execute(task);
                // We don't need to parse pending
                // (async tasks must live with race-conditions if they attempt to cancel between these few lines of code)
            }
            final long period = task.getPeriod(); // State consistency
            if (period > 0) {
                task.setNextRun(currentTick + period);
                temp.add(task);
            } else if (task.isSync()) {
                runners.remove(task.getTaskId());
            }
        }
        pending.addAll(temp);
        temp.clear();
        debugHead = debugHead.getNextHead(currentTick);
    }

    private void addTask(final CraftTask task) {
        final AtomicReference<CraftTask> tail = this.tail;
        CraftTask tailTask = tail.get();
        while (!tail.compareAndSet(tailTask, task)) {
            tailTask = tail.get();
        }
        tailTask.setNext(task);
    }

    private CraftTask handle(final CraftTask task, final long delay) {
        task.setNextRun(currentTick + delay);
        addTask(task);
        return task;
    }

    private static void validate(final Plugin plugin, final Object task) {
        Validate.notNull(plugin, "Plugin cannot be null");
        Validate.notNull(task, "Task cannot be null");
        if (!plugin.isEnabled()) {
            throw new InvalidPluginException("Plugin attempted to register task while disabled");
        }
    }

    private int nextId() {
        return ids.incrementAndGet();
    }

    private void parsePending() {
        CraftTask head = this.head;
        CraftTask task = head.getNext();
        CraftTask lastTask = head;
        for (; task != null; task = (lastTask = task).getNext()) {
            if (task.getTaskId() == -1) {
                task.run();
            } else if (task.getPeriod() >= -1) {
                pending.add(task);
                runners.put(task.getTaskId(), task);
            }
        }
        // We split this because of the way things are ordered for all of the async calls in CraftScheduler
        // (it prevents race-conditions)
        for (task = head; task != lastTask; task = head) {
           head = task.getNext();
           task.setNext(null);
        }
        this.head = lastTask;
    }

    private boolean isReady(final int currentTick) {
        return !pending.isEmpty() && pending.peek().getNextRun() <= currentTick;
    }

    @Override
    public String toString() {
        int debugTick = currentTick;
        StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - RECENT_TICKS).append('-').append(debugTick).append('{');
        debugHead.debugTo(string);
        return string.append('}').toString();
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, LRunnable task, long delay) {
        return scheduleSyncDelayedTask(plugin, (Runnable) task, delay);
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(Plugin plugin, LRunnable task) {
        return scheduleSyncDelayedTask(plugin, (Runnable) task);
    }

    @Deprecated
    @Override
    public int scheduleSyncRepeatingTask(Plugin plugin, LRunnable task, long delay, long period) {
        return scheduleSyncRepeatingTask(plugin, (Runnable) task, delay, period);
    }

    @Deprecated
    @Override
    public LTask runTask(Plugin plugin, LRunnable task) throws IllegalArgumentException {
        return runTask(plugin, (Runnable) task);
    }

    @Deprecated
    @Override
    public LTask runTaskAsynchronously(Plugin plugin, LRunnable task) throws IllegalArgumentException {
        return runTaskAsynchronously(plugin, (Runnable) task);
    }

    @Deprecated
    @Override
    public LTask runTaskLater(Plugin plugin, LRunnable task, long delay) throws IllegalArgumentException {
        return runTaskLater(plugin, (Runnable) task, delay);
    }

    @Deprecated
    @Override
    public LTask runTaskLaterAsynchronously(Plugin plugin, LRunnable task, long delay) throws IllegalArgumentException {
        return runTaskLaterAsynchronously(plugin, (Runnable) task, delay);
    }

    @Deprecated
    @Override
    public LTask runTaskTimer(Plugin plugin, LRunnable task, long delay, long period) throws IllegalArgumentException {
        return runTaskTimer(plugin, (Runnable) task, delay, period);
    }

    @Deprecated
    @Override
    public LTask runTaskTimerAsynchronously(Plugin plugin, LRunnable task, long delay, long period) throws IllegalArgumentException {
        return runTaskTimerAsynchronously(plugin, (Runnable) task, delay, period);
    }
}
