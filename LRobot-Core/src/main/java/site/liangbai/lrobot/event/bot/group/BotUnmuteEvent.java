package site.liangbai.lrobot.event.bot.group;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class BotUnmuteEvent extends Event {
    private final Group group;
    private final GroupMember operator;

    public BotUnmuteEvent(net.mamoe.mirai.event.events.BotUnmuteEvent event) {
        this.group = new Group(event.getGroup().getId());
        this.operator = this.group.getGroupMember(event.getOperator().getId());
    }

    public Group getGroup() {
        return group;
    }

    public GroupMember getOperator() {
        return operator;
    }
}
