package site.liangbai.lrobot.event.group.member;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberUnmuteEvent extends MemberEvent {
    private final GroupMember whoUnmute;

    public MemberUnmuteEvent(net.mamoe.mirai.event.events.MemberUnmuteEvent event) {
        super(new Group(event.getGroup().getId()), new GroupMember(event.getGroup().getId(), event.getMember().getId()));
        whoUnmute = new GroupMember(event.getGroup().getId(), event.getOperator() == null ? event.getBot().getId() : event.getOperator().getId());
    }

    public GroupMember getWhoUnmute() {
        return whoUnmute;
    }
}
