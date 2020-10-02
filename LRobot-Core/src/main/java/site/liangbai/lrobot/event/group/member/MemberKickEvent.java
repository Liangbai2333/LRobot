package site.liangbai.lrobot.event.group.member;

import net.mamoe.mirai.event.events.MemberLeaveEvent;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberKickEvent extends Event {
    private final Group group;
    private final GroupMember whoKicked;
    private final MemberLeaveEvent.Kick event;

    public MemberKickEvent(MemberLeaveEvent.Kick event){
        this.event = event;
        group = new Group(event.getGroup().getId());
        whoKicked = new GroupMember(event.getGroup().getId(), event.getOperator() == null ? event.getBot().getId() : event.getOperator().getId());
    }

    public Group getGroup() {
        return group;
    }

    public GroupMember getWhoKicked() {
        return whoKicked;
    }

    public long getLeaveId(){
        return event.getMember().getId();
    }

    public String getLeaveName(){
        return event.getMember().getNick();
    }
}
