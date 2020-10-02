package site.liangbai.lrobot.event.service;

import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.Plugin;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public final class PluginDisableEvent extends PluginEvent {
    public PluginDisableEvent(Plugin plugin) {
        super(plugin);
    }
}
