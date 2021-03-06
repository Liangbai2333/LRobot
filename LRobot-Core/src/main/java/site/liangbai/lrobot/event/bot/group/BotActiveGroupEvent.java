package site.liangbai.lrobot.event.bot.group;

import net.mamoe.mirai.event.events.BotLeaveEvent;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class BotActiveGroupEvent extends Event {
    private final Group group;

    public BotActiveGroupEvent(BotLeaveEvent.Active event) {
        this.group = new Group(event.getGroup().getId());
    }

    public Group getGroup() {
        return group;
    }
}
