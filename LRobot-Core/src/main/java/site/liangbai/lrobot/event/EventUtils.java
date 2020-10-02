package site.liangbai.lrobot.event;

import org.apache.commons.lang3.Validate;
import site.liangbai.lrobot.event.bot.group.*;
import site.liangbai.lrobot.event.friend.*;
import site.liangbai.lrobot.event.group.*;
import site.liangbai.lrobot.event.group.member.*;
import site.liangbai.lrobot.event.message.*;
import site.liangbai.lrobot.plugin.EventExecutor;
import site.liangbai.lrobot.plugin.Plugin;
import site.liangbai.lrobot.plugin.PluginManager;
import site.liangbai.lrobot.plugin.RegisteredListener;
import site.liangbai.lrobot.plugin.annotation.EventObject;
import site.liangbai.lrobot.plugin.exception.InvalidPluginException;

import java.util.*;

public class EventUtils {
    public static final Map<String, Class<? extends Event>> registeredListener = new HashMap<>();
    public static final Map<Class<?>, HandlerList> handlers = new HashMap<>();
	
	static {
        registeredListener.put("GroupMessageEvent", GroupMessageEvent.class);
        registeredListener.put("FriendMessageEvent", FriendMessageEvent.class);
        registeredListener.put("TempMessageEvent", TempMessageEvent.class);
        registeredListener.put("FriendAddEvent", FriendAddEvent.class);
        registeredListener.put("FriendAvatarChangedEvent", FriendAvatarChangedEvent.class);
        registeredListener.put("FriendRemarkChangeEvent", FriendRemarkChangeEvent.class);
        registeredListener.put("GroupAllowAnonymousChatEvent", GroupAllowAnonymousChatEvent.class);
        registeredListener.put("GroupAllowMemberInviteEvent", GroupAllowMemberInviteEvent.class);
        registeredListener.put("GroupEntranceAnnouncementChangeEvent", GroupEntranceAnnouncementChangeEvent.class);
        registeredListener.put("GroupMuteAllEvent", GroupMuteAllEvent.class);
        registeredListener.put("GroupNameChangeEvent", GroupNameChangeEvent.class);
        registeredListener.put("MemberCardChangeEvent", MemberCardChangeEvent.class);
        registeredListener.put("MemberJoinEvent", MemberJoinEvent.class);
        registeredListener.put("MemberMuteEvent", MemberMuteEvent.class);
        registeredListener.put("MemberPermissionChangeEvent", MemberPermissionChangeEvent.class);
        registeredListener.put("MemberSpecialTitleChangeEvent", MemberSpecialTitleChangeEvent.class);
        registeredListener.put("MemberUnmuteEvent", MemberUnmuteEvent.class);
        registeredListener.put("BotGroupPermissionChangeEvent", BotGroupPermissionChangeEvent.class);
        registeredListener.put("BotJoinGroupEvent", BotJoinGroupEvent.class);
        registeredListener.put("BotMuteEvent", BotMuteEvent.class);
        registeredListener.put("BotUnmuteEvent", BotUnmuteEvent.class);

        // anonymous
        registeredListener.put("net.mamoe.mirai.event.events.MemberLeaveEvent$Kick", MemberKickEvent.class);
        registeredListener.put("net.mamoe.mirai.event.events.MemberLeaveEvent$Quit", MemberQuitEvent.class);
        registeredListener.put("net.mamoe.mirai.event.events.BotLeaveEvent$Active", BotActiveGroupEvent.class);
        registeredListener.put("net.mamoe.mirai.event.events.BotLeaveEvent$Kick", BotKickGroupEvent.class);
	}

	public static synchronized void registerEvents(Plugin plugin, Class<?> listener) {
        if (!plugin.isEnabled()) {
            throw new InvalidPluginException("Plugin attempted to register " + listener + " while not enabled");
        }

        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin, true).entrySet()) {
            getEventListeners(entry.getKey()).registerAll(entry.getValue());
        }
    }

    public static void callEvent(Event event) {
        if (event.isAsynchronous()) {
            if (Thread.holdsLock(EventUtils.class)) {
                throw new IllegalStateException(event.getEventName() + " cannot be triggered asynchronously from inside synchronized code.");
            }
            fireEvent(event);
        } else {
            synchronized (EventUtils.class) {
                fireEvent(event);
            }
        }
    }

    private static HandlerList getHandlerList(Class<? extends Event> event) {
	    EventObject handlerListType = event.getAnnotation(EventObject.class);
	    Validate.notNull(handlerListType, "Event " + event.getSimpleName() + " must have annotation Event!");
	    if(handlerListType.handler() == HandlerListType.BUS) {
	        return Event.BUS_HANDLERLIST;
        }
	    if(handlerListType.handler() == HandlerListType.NEW) {
	        if(!handlers.containsKey(event)) {
	            handlers.put(event, new HandlerList());
            }
	        return handlers.get(event);
        }
	    if(handlerListType.handler() == HandlerListType.SUPER) {
	        Validate.notNull(event.getSuperclass(), "Event " + event.getSimpleName() + " super class can not be null");
	        if(!Event.class.isAssignableFrom(event.getSuperclass())) {
	            throw new InvalidPluginException("Event " + event.getSimpleName() + " super class must extends Event");
            }
	        return getHandlerList(event.getSuperclass().asSubclass(Event.class));
        }
	    return null;
    }

    private static void fireEvent(Event event) {
        HandlerList handlers = getHandlerList(event.getClass());
        Validate.notNull(handlers, "Event " + event.getEventName() + " handlerList be null");
        RegisteredListener[] listeners = handlers.getRegisteredListeners();
        for (RegisteredListener registration : listeners) {
            if (!registration.getPlugin().isEnabled()) {
                continue;
            }

            try {
                registration.callEvent(event);
            } catch (Throwable ex) {
                PluginManager.logger.error("Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getName(), ex);
            }
        }
    }

    public static void registerEvents(Plugin plugin, Listener listener) {
        if (!plugin.isEnabled()) {
            throw new InvalidPluginException("Plugin attempted to register " + listener + " while not enabled");
        }

        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin, false).entrySet()) {
            getEventListeners(entry.getKey()).registerAll(entry.getValue());
        }
    }

    public static void registerEvent(Class<? extends Event> event, EventPriority priority, EventExecutor executor, Plugin plugin, Listener listener) {
        registerEvent(event, priority, executor, plugin, false, listener);
    }

    public static void registerEvent(Class<? extends Event> event, EventPriority priority, EventExecutor executor, Plugin plugin, boolean ignoreCancelled, Listener listener) {
        Validate.notNull(listener, "Listener cannot be null");
        Validate.notNull(priority, "Priority cannot be null");
        Validate.notNull(executor, "Executor cannot be null");
        Validate.notNull(plugin, "Plugin cannot be null");

        if (!plugin.isEnabled()) {
            throw new InvalidPluginException("Plugin attempted to register " + event + " while not enabled");
        }

        getEventListeners(event).register(new RegisteredListener(listener, executor, priority, plugin, ignoreCancelled));
    }

    private static HandlerList getEventListeners(Class<? extends Event> type) {
        HandlerList handlerList = getHandlerList(type);
        Validate.notNull(handlerList, "Event " + type.getSimpleName() + " handlerList be null");
        return handlerList;
    }
}
