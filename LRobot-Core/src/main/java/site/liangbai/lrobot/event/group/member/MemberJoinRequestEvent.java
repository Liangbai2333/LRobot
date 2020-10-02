package site.liangbai.lrobot.event.group.member;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.event.Cancellable;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberJoinRequestEvent extends Event implements Cancellable {
    private final net.mamoe.mirai.event.events.MemberJoinRequestEvent event;
    private boolean cancelled = true;

    public MemberJoinRequestEvent(net.mamoe.mirai.event.events.MemberJoinRequestEvent event){
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

    public Group getGroup(){
        return new Group(event.getGroup().getId());
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
