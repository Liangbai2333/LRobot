package site.liangbai.lrobot.event.service;

import site.liangbai.lrobot.event.Cancellable;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.Plugin;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class PluginLoadEvent extends PluginEvent implements Cancellable {
    private boolean cancelled;

    public PluginLoadEvent(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
