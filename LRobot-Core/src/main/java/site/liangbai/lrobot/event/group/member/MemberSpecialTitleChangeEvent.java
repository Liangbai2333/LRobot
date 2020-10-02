package site.liangbai.lrobot.event.group.member;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberSpecialTitleChangeEvent extends MemberEvent {
    private final String old;
    private final String New;

    public MemberSpecialTitleChangeEvent(net.mamoe.mirai.event.events.MemberSpecialTitleChangeEvent event) {
        super(new Group(event.getGroup().getId()), new GroupMember(event.getGroup().getId(), event.getMember().getId()));
        old = event.getOrigin();
        New = event.getNew();
    }

    public String getNew() {
        return New;
    }

    public String getOld() {
        return old;
    }
}
