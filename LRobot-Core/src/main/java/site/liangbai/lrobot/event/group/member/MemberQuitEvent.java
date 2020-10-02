package site.liangbai.lrobot.event.group.member;

import net.mamoe.mirai.event.events.MemberLeaveEvent;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberQuitEvent extends Event {
    private final Group group;
    private final MemberLeaveEvent.Quit event;

    public MemberQuitEvent(MemberLeaveEvent.Quit event){
        this.event = event;
        group = new Group(event.getGroup().getId());
    }

    public Group getGroup() {
        return group;
    }

    public long getLeaveId(){
        return event.getMember().getId();
    }

    public String getLeaveName(){
        return event.getMember().getNick();
    }
}
