package site.liangbai.lrobot.plugin;

import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.EventException;

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface EventExecutor {
    public void execute(Object listener, Event event) throws EventException;
}
