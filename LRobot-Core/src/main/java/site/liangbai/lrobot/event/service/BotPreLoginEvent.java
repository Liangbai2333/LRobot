package site.liangbai.lrobot.event.service;

import net.mamoe.mirai.utils.BotConfiguration;
import site.liangbai.lrobot.event.Cancellable;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class BotPreLoginEvent extends Event implements Cancellable {
    private final long qq;
    private final BotConfiguration botConfiguration;
    private boolean cancelled;

    public BotPreLoginEvent(long qq, BotConfiguration configuration) {
        this.botConfiguration = configuration;
        this.qq = qq;
    }

    public BotConfiguration getBotConfiguration() {
        return botConfiguration;
    }

    public long getQQ() {
        return qq;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
