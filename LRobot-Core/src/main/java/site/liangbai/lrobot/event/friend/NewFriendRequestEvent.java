package site.liangbai.lrobot.event.friend;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.event.Cancellable;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class NewFriendRequestEvent extends Event implements Cancellable {
    private final net.mamoe.mirai.event.events.NewFriendRequestEvent event;
    private boolean cancelled = true;

    public NewFriendRequestEvent(net.mamoe.mirai.event.events.NewFriendRequestEvent event){
        this.event = event;
    }

    public String getRequestMessage(){
        return event.getMessage();
    }

    public long getId(){
        return event.getFromId();
    }

    public String getName(){
        return event.getFromNick();
    }

    public Group getFromGroup(){
        return event.getFromGroup() != null ?  new Group(event.getFromGroup().getId()) : null;
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
