package site.liangbai.lrobot.event.friend;

import site.liangbai.lrobot.contact.Friend;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class FriendRemarkChangeEvent extends FriendEvent {
    private final String newName;

    public FriendRemarkChangeEvent(net.mamoe.mirai.event.events.FriendRemarkChangeEvent event){
        super(new Friend(event.getFriend().getId()));
        this.newName = event.getNewName();
    }

    public String getNewName() {
        return newName;
    }
}
