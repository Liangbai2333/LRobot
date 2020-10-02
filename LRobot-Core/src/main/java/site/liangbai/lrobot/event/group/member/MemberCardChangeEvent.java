package site.liangbai.lrobot.event.group.member;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberCardChangeEvent extends MemberEvent {
    private final String old;
    private final String New;

    public MemberCardChangeEvent(net.mamoe.mirai.event.events.MemberCardChangeEvent event){
        super(new Group(event.getGroup().getId()), new GroupMember(event.getGroup().getId(), event.getMember().getId()));
        this.old = event.getOrigin();
        this.New = event.getNew();
    }
    public String getNew() {
        return New;
    }

    public String getOld() {
        return old;
    }
}
