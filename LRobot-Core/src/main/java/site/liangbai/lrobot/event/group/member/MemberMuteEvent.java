package site.liangbai.lrobot.event.group.member;

import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class MemberMuteEvent extends MemberEvent {
    private final GroupMember whoMute;
    private final int muteSeconds;

    public MemberMuteEvent(net.mamoe.mirai.event.events.MemberMuteEvent event){
        super(new Group(event.getGroup().getId()), new GroupMember(event.getGroup().getId(), event.getMember().getId()));
        whoMute = new GroupMember(event.getGroup().getId(), event.getOperator() == null ? event.getBot().getId() : event.getOperator().getId());
        muteSeconds = event.getDurationSeconds();
    }

    public GroupMember getWhoMute() {
        return whoMute;
    }

    public int getMuteSeconds() {
        return muteSeconds;
    }
}
