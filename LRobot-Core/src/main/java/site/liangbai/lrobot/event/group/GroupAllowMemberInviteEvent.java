package site.liangbai.lrobot.event.group;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class GroupAllowMemberInviteEvent extends GroupEvent {
    private final boolean old;
    private final boolean New;
    private final GroupMember whoChange;

    public GroupAllowMemberInviteEvent(net.mamoe.mirai.event.events.GroupAllowMemberInviteEvent event){
        super(new Group(event.getGroup().getId()));
        this.old = event.getOrigin();
        this.New = event.getNew();
        this.whoChange = new GroupMember(event.getGroup().getId(), event.getOperator() == null ? event.getBot().getId() : event.getOperator().getId());
    }

    public boolean getNew() {
        return New;
    }

    public boolean getOld() {
        return old;
    }

    public GroupMember getWhoChange() {
        return whoChange;
    }
}
