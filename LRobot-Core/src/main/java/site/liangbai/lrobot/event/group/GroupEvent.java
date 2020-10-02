package site.liangbai.lrobot.event.group;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public abstract class GroupEvent extends Event {
    private final Group group;

    public GroupEvent(Group group){
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }
}
