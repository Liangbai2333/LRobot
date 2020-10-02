package site.liangbai.lrobot.scheduler;

import site.liangbai.lrobot.plugin.Plugin;

/**
 * Represents a task being executed by the scheduler
 */
public interface LTask {

    /**
     * Returns the taskId for the task.
     *
     * @return Task id number
     */
    public int getTaskId();

    /**
     * Returns the Plugin that owns this task.
     *
     * @return The Plugin that owns the task
     */
    public Plugin getOwner();

    /**
     * Returns true if the Task is a sync task.
     *
     * @return true if the task is run by main thread
     */
    public boolean isSync();

    /**
     * Will attempt to cancel this task.
     */
    public void cancel();
}
