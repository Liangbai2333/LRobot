package site.liangbai.lrobot.scheduler;

import site.liangbai.lrobot.plugin.Plugin;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface LScheduler {

    /**
     * Schedules a once off task to occur after a delay.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay);

    /**
     * @deprecated Use {@link LRunnable#runTaskLater(Plugin, long)}
     */
    @Deprecated
    public int scheduleSyncDelayedTask(Plugin plugin, LRunnable task, long delay);

    /**
     * Schedules a once off task to occur as soon as possible.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncDelayedTask(Plugin plugin, Runnable task);

    /**
     * @deprecated Use {@link LRunnable#runTask(Plugin)}
     */
    @Deprecated
    public int scheduleSyncDelayedTask(Plugin plugin, LRunnable task);

    /**
     * Schedules a repeating task.
     * <p>
     * This task will be executed by the main server thread.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     */
    public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

    /**
     * @deprecated Use {@link LRunnable#runTaskTimer(Plugin, long, long)}
     */
    @Deprecated
    public int scheduleSyncRepeatingTask(Plugin plugin, LRunnable task, long delay, long period);

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules a once off task to occur after a delay. This task will be
     * executed by a thread managed by the scheduler.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing task
     * @return Task id number (-1 if scheduling failed)
     * @deprecated This name is misleading, as it does not schedule "a sync"
     *     task, but rather, "an async" task
     */
    @Deprecated
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay);

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules a once off task to occur as soon as possible. This task will
     * be executed by a thread managed by the scheduler.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Task id number (-1 if scheduling failed)
     * @deprecated This name is misleading, as it does not schedule "a sync"
     *     task, but rather, "an async" task
     */
    @Deprecated
    public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task);

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules a repeating task. This task will be executed by a thread
     * managed by the scheduler.
     *
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @param delay Delay in server ticks before executing first repeat
     * @param period Period in server ticks of the task
     * @return Task id number (-1 if scheduling failed)
     * @deprecated This name is misleading, as it does not schedule "a sync"
     *     task, but rather, "an async" task
     */
    @Deprecated
    public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

    /**
     * Calls a method on the main thread and returns a Future object. This
     * task will be executed by the main server thread.
     * <ul>
     * <li>Note: The Future.get() methods must NOT be called from the main
     *     thread.
     * <li>Note2: There is at least an average of 10ms latency until the
     *     isDone() method returns true.
     * </ul>
     * @param <T> The callable's return type
     * @param plugin Plugin that owns the task
     * @param task Task to be executed
     * @return Future Future object related to the task
     */
    public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task);

    /**
     * Removes task from scheduler.
     *
     * @param taskId Id number of task to be removed
     */
    public void cancelTask(int taskId);

    /**
     * Removes all tasks associated with a particular plugin from the
     * scheduler.
     *
     * @param plugin Owner of tasks to be removed
     */
    public void cancelTasks(Plugin plugin);

    /**
     * Removes all tasks from the scheduler.
     */
    public void cancelAllTasks();

    /**
     * Check if the task currently running.
     * <p>
     * A repeating task might not be running currently, but will be running in
     * the future. A task that has finished, and does not repeat, will not be
     * running ever again.
     * <p>
     * Explicitly, a task is running if there exists a thread for it, and that
     * thread is alive.
     *
     * @param taskId The task to check.
     * <p>
     * @return If the task is currently running.
     */
    public boolean isCurrentlyRunning(int taskId);

    /**
     * Check if the task queued to be run later.
     * <p>
     * If a repeating task is currently running, it might not be queued now
     * but could be in the future. A task that is not queued, and not running,
     * will not be queued again.
     *
     * @param taskId The task to check.
     * <p>
     * @return If the task is queued to be run.
     */
    public boolean isQueued(int taskId);

    /**
     * Returns a list of all active workers.
     * <p>
     * This list contains asynch tasks that are being executed by separate
     * threads.
     *
     * @return Active workers
     */
    public List<LWorker> getActiveWorkers();

    /**
     * Returns a list of all pending tasks. The ordering of the tasks is not
     * related to their order of execution.
     *
     * @return Active workers
     */
    public List<LTask> getPendingTasks();

    /**
     * Returns a task that will run on the next server tick.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public LTask runTask(Plugin plugin, Runnable task) throws IllegalArgumentException;

    /**
     * @deprecated Use {@link LRunnable#runTask(Plugin)}
     */
    @Deprecated
    public LTask runTask(Plugin plugin, LRunnable task) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public LTask runTaskAsynchronously(Plugin plugin, Runnable task) throws IllegalArgumentException;

    /**
     * @deprecated Use {@link LRunnable#runTaskAsynchronously(Plugin)}
     */
    @Deprecated
    public LTask runTaskAsynchronously(Plugin plugin, LRunnable task) throws IllegalArgumentException;

    /**
     * Returns a task that will run after the specified number of server
     * ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public LTask runTaskLater(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException;

    /**
     * @deprecated Use {@link LRunnable#runTaskLater(Plugin, long)}
     */
    @Deprecated
    public LTask runTaskLater(Plugin plugin, LRunnable task, long delay) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will run asynchronously after the specified number
     * of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public LTask runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException;

    /**
     * @deprecated Use {@link LRunnable#runTaskLaterAsynchronously(Plugin, long)}
     */
    @Deprecated
    public LTask runTaskLaterAsynchronously(Plugin plugin, LRunnable task, long delay) throws IllegalArgumentException;

    /**
     * Returns a task that will repeatedly run until cancelled, starting after
     * the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public LTask runTaskTimer(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException;

    /**
     * @deprecated Use {@link LRunnable#runTaskTimer(Plugin, long, long)}
     */
    @Deprecated
    public LTask runTaskTimer(Plugin plugin, LRunnable task, long delay, long period) throws IllegalArgumentException;

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Returns a task that will repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param task the task to be run
     * @param delay the ticks to wait before running the task for the first
     *     time
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalArgumentException if task is null
     */
    public LTask runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period) throws IllegalArgumentException;

    /**
     * @deprecated Use {@link LRunnable#runTaskTimerAsynchronously(Plugin, long, long)}
     */
    @Deprecated
    public LTask runTaskTimerAsynchronously(Plugin plugin, LRunnable task, long delay, long period) throws IllegalArgumentException;
}
