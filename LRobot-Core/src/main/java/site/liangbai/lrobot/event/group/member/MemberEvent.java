package site.liangbai.lrobot.event.group.member;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public abstract class MemberEvent extends Event {
    private final Group group;
    private final GroupMember member;

    public MemberEvent(Group group, GroupMember member){
        this.group = group;
        this.member = member;
    }

    public GroupMember getMember() {
        return member;
    }

    public Group getGroup() {
        return group;
    }
}
