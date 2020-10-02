package site.liangbai.lrobot.event.bot.group;

import net.mamoe.mirai.contact.MemberPermission;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.permission.GroupPermission;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class BotGroupPermissionChangeEvent extends Event {
    private final GroupPermission old;
    private final GroupPermission New;
    private final Group group;

    public BotGroupPermissionChangeEvent(net.mamoe.mirai.event.events.BotGroupPermissionChangeEvent event){
        group = new Group(event.getGroup().getId());
        old = event.getOrigin() == MemberPermission.OWNER ? GroupPermission.OWNER : event.getOrigin() == MemberPermission.ADMINISTRATOR ? GroupPermission.ADMINISTRATOR : GroupPermission.MEMBER;
        New = event.getNew() == MemberPermission.OWNER ? GroupPermission.OWNER : event.getOrigin() == MemberPermission.ADMINISTRATOR ? GroupPermission.ADMINISTRATOR : GroupPermission.MEMBER;
    }

    public Group getGroup() {
        return group;
    }

    public GroupPermission getOld() {
        return old;
    }

    public GroupPermission getNew() {
        return New;
    }
}
