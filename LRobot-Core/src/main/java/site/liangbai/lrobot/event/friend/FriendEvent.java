package site.liangbai.lrobot.event.friend;

import site.liangbai.lrobot.contact.Friend;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public abstract class FriendEvent extends Event {
    private final Friend friend;

    public FriendEvent(Friend friend){
        this.friend = friend;
    }

    public Friend getFriend() {
        return friend;
    }
}
