package site.liangbai.lrobot.event.friend;

import site.liangbai.lrobot.contact.Friend;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class FriendAddEvent extends FriendEvent {

    public FriendAddEvent(net.mamoe.mirai.event.events.FriendAddEvent event){
        super(new Friend(event.getFriend().getId()));
    }
}
