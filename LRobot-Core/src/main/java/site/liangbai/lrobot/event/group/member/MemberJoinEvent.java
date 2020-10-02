package site.liangbai.lrobot.event.group.member;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberJoinEvent extends MemberEvent {

    public MemberJoinEvent(net.mamoe.mirai.event.events.MemberJoinEvent event){
        super(new Group(event.getGroup().getId()), new GroupMember(event.getGroup().getId(), event.getMember().getId()));
    }
}
