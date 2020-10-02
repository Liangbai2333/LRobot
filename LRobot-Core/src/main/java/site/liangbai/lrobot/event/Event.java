package site.liangbai.lrobot.event;

import site.liangbai.lrobot.plugin.Plugin;
import site.liangbai.lrobot.plugin.annotation.EventObject;

/**
 * Represents an event.
 *
 * @see EventUtils#callEvent(Event)
 * @see EventUtils#registerEvents(Listener, Plugin)
 */
@EventObject(handler = HandlerListType.BUS)
public abstract class Event {
    public static final HandlerList BUS_HANDLERLIST = new HandlerList();
    private String name;
    private final boolean async;

    /**
     * The default constructor is defined for cleaner code. This constructor
     * assumes the event is synchronous.
     */
    public Event() {
        this(false);
    }

    /**
     * This constructor is used to explicitly declare an event as synchronous
     * or asynchronous.
     *
     * @param isAsync true indicates the event will fire asynchronously, false
     *     by default from default constructor
     */
    public Event(boolean isAsync) {
        this.async = isAsync;
    }

    /**
     * Convenience method for providing a user-friendly identifier. By
     * default, it is the event's class's {@linkplain Class#getSimpleName()
     * simple name}.
     *
     * @return name of this event
     */
    public String getEventName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }
        return name;
    }

    /**
     * Any custom event that should not by synchronized with other events must
     * use the specific constructor. These are the caveats of using an
     * asynchronous event:
     * <ul>
     * <li>The event is never fired from inside code triggered by a
     *     synchronous event. Attempting to do so results in an {@link
     *     java.lang.IllegalStateException}.
     * <li>However, asynchronous event handlers may fire synchronous or
     *     asynchronous events
     * <li>The event may be fired multiple times simultaneously and in any
     *     order.
     * <li>Any newly registered or unregistered handler is ignored after an
     *     event starts execution.
     * <li>The handlers for this event may block for any length of time.
     * <li>Some implementations may selectively declare a specific event use
     *     as asynchronous. This behavior should be clearly defined.
     * <li>Asynchronous calls are not calculated in the plugin timing system.
     * </ul>
     *
     * @return false by default, true if the event fires asynchronously
     */
    public final boolean isAsynchronous() {
        return async;
    }
}
