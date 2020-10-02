package site.liangbai.lrobot.event.group.member;

import net.mamoe.mirai.contact.MemberPermission;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.permission.GroupPermission;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberPermissionChangeEvent extends MemberEvent {
    private final GroupPermission old;
    private final GroupPermission New;

    public MemberPermissionChangeEvent(net.mamoe.mirai.event.events.MemberPermissionChangeEvent event){
        super(new Group(event.getGroup().getId()), new GroupMember(event.getGroup().getId(), event.getMember().getId()));
        old = event.getOrigin() == MemberPermission.OWNER ? GroupPermission.OWNER : event.getOrigin() == MemberPermission.ADMINISTRATOR ? GroupPermission.ADMINISTRATOR : GroupPermission.MEMBER;
        New = event.getNew() == MemberPermission.OWNER ? GroupPermission.OWNER : event.getOrigin() == MemberPermission.ADMINISTRATOR ? GroupPermission.ADMINISTRATOR : GroupPermission.MEMBER;
    }

    public GroupPermission getNew() {
        return New;
    }

    public GroupPermission getOld() {
        return old;
    }
}
