package site.liangbai.lrobot.event.bot.group;

import site.liangbai.lrobot.contact.Friend;
import site.liangbai.lrobot.event.Cancellable;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class BotInvitedJoinGroupRequestEvent extends Event implements Cancellable {
    private final net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent event;
    private final Friend friend;
    private boolean cancelled = true;

    public BotInvitedJoinGroupRequestEvent(net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent event) {
        this.event = event;
        this.friend = new Friend(event.getInvitorId());
    }

    public Friend getFriend() {
        return this.friend;
    }

    public long getGroupId() {
        return event.getGroupId();
    }

    public String getGroupName() {
        return event.getGroupName();
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
