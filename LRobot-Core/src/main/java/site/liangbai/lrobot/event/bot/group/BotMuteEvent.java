package site.liangbai.lrobot.event.bot.group;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class BotMuteEvent extends Event {
    private final Group group;
    private final int durationSeconds;
    private final GroupMember operator;

    public BotMuteEvent(net.mamoe.mirai.event.events.BotMuteEvent event) {
        this.group = new Group(event.getGroup().getId());
        this.durationSeconds = event.getDurationSeconds();
        this.operator = this.group.getGroupMember(event.getOperator().getId());
    }

    public Group getGroup() {
        return group;
    }

    public GroupMember getOperator() {
        return operator;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }
}
