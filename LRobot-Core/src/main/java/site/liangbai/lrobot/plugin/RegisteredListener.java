package site.liangbai.lrobot.plugin;

import site.liangbai.lrobot.event.*;

/**
 * Stores relevant information for plugin listeners
 */
public class RegisteredListener {
    private final Object obj;
    private final EventPriority priority;
    private final Plugin plugin;
    private EventExecutor executor;
    private final boolean ignoreCancelled;

    public RegisteredListener(final Object listener, final EventExecutor executor, final EventPriority priority, final Plugin plugin, final boolean ignoreCancelled) {
        this.obj = listener;
        this.priority = priority;
        this.plugin = plugin;
        this.executor = executor;
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * Gets the listener for this registration
     *
     * @return Registered Listener
     */
    public Object getListener() {
        return obj;
    }

    /**
     * Gets the plugin for this registration
     *
     * @return Registered Plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the priority for this registration
     *
     * @return Registered Priority
     */
    public EventPriority getPriority() {
        return priority;
    }

    /**
     * Calls the event executor
     *
     * @param event The event
     * @throws EventException If an event handler throws an exception.
     */
    public void callEvent(final Event event) throws EventException {
        if (event instanceof Cancellable){
            if (((Cancellable) event).isCancelled() && isIgnoringCancelled()){
                return;
            }
        }
        executor.execute(obj, event);
    }

    /**
     * Whether this listener accepts cancelled events
     *
     * @return True when ignoring cancelled events
     */
    public boolean isIgnoringCancelled() {
        return ignoreCancelled;
    }

    public EventExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(EventExecutor executor) {
        this.executor = executor;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }
}
