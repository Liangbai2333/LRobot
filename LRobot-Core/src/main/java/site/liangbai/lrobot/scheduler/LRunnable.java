package site.liangbai.lrobot.scheduler;

import site.liangbai.lrobot.LRobot;
import site.liangbai.lrobot.plugin.Plugin;

/**
 * This class is provided as an easy way to handle scheduling tasks.
 */
public abstract class LRunnable implements Runnable {
    private int taskId = -1;

    /**
     * Attempts to cancel this task.
     *
     * @throws IllegalStateException if task was not scheduled yet
     */
    public synchronized void cancel() throws IllegalStateException {
        LRobot.getScheduler().cancelTask(getTaskId());
    }

    /**
     * Schedules this in the Bukkit scheduler to run on next tick.
     *
     * @param plugin the reference to the plugin scheduling task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see LScheduler#runTask(Plugin, Runnable)
     */
    public synchronized LTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        checkState();
        return setupId(LRobot.getScheduler().runTask(plugin, (Runnable) this));
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this in the Bukkit scheduler to run asynchronously.
     *
     * @param plugin the reference to the plugin scheduling task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see LScheduler#runTaskAsynchronously(Plugin, Runnable)
     */
    public synchronized LTask runTaskAsynchronously(Plugin plugin) throws IllegalArgumentException, IllegalStateException  {
        checkState();
        return setupId(LRobot.getScheduler().runTaskAsynchronously(plugin, (Runnable) this));
    }

    /**
     * Schedules this to run after the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see LScheduler#runTaskLater(Plugin, Runnable, long)
     */
    public synchronized LTask runTaskLater(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException  {
        checkState();
        return setupId(LRobot.getScheduler().runTaskLater(plugin, (Runnable) this, delay));
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to run asynchronously after the specified number of
     * server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see LScheduler#runTaskLaterAsynchronously(Plugin, Runnable, long)
     */
    public synchronized LTask runTaskLaterAsynchronously(Plugin plugin, long delay) throws IllegalArgumentException, IllegalStateException  {
        checkState();
        return setupId(LRobot.getScheduler().runTaskLaterAsynchronously(plugin, (Runnable) this, delay));
    }

    /**
     * Schedules this to repeatedly run until cancelled, starting after the
     * specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see LScheduler#runTaskTimer(Plugin, Runnable, long, long)
     */
    public synchronized LTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException  {
        checkState();
        return setupId(LRobot.getScheduler().runTaskTimer(plugin, (Runnable) this, delay, period));
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to repeatedly run asynchronously until cancelled,
     * starting after the specified number of server ticks.
     *
     * @param plugin the reference to the plugin scheduling task
     * @param delay the ticks to wait before running the task for the first
     *     time
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if this was already scheduled
     * @see LScheduler#runTaskTimerAsynchronously(Plugin, Runnable, long,
     *     long)
     */
    public synchronized LTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException  {
        checkState();
        return setupId(LRobot.getScheduler().runTaskTimerAsynchronously(plugin, (Runnable) this, delay, period));
    }

    /**
     * Gets the task id for this runnable.
     *
     * @return the task id that this runnable was scheduled as
     * @throws IllegalStateException if task was not scheduled yet
     */
    public synchronized int getTaskId() throws IllegalStateException {
        final int id = taskId;
        if (id == -1) {
            throw new IllegalStateException("Not scheduled yet");
        }
        return id;
    }

    private void checkState() {
        if (taskId != -1) {
            throw new IllegalStateException("Already scheduled as " + taskId);
        }
    }

    private LTask setupId(final LTask task) {
        this.taskId = task.getTaskId();
        return task;
    }
}
