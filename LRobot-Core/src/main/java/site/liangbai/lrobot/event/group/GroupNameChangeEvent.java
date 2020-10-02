package site.liangbai.lrobot.event.group;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class GroupNameChangeEvent extends GroupEvent {
    private final String old;
    private final String New;
    private final GroupMember whoChange;

    public GroupNameChangeEvent(net.mamoe.mirai.event.events.GroupNameChangeEvent event){
        super(new Group(event.getGroup().getId()));
        this.old = event.getOrigin();
        this.New = event.getNew();
        this.whoChange = new GroupMember(event.getGroup().getId(), event.getOperator() == null ? event.getBot().getId() : event.getOperator().getId());
    }

    public String getNew() {
        return New;
    }

    public String getOld() {
        return old;
    }

    public GroupMember getWhoChange() {
        return whoChange;
    }
}
