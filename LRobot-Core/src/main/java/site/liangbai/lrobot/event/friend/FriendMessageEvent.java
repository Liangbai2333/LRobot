package site.liangbai.lrobot.event.friend;

import net.mamoe.mirai.message.data.QuoteReply;
import site.liangbai.lrobot.contact.Friend;
import site.liangbai.lrobot.contact.Message;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.message.Chain;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class FriendMessageEvent extends FriendEvent {
    private final net.mamoe.mirai.contact.Friend friend;
    private final Chain chain;
    private final QuoteReply reply;

    public FriendMessageEvent(net.mamoe.mirai.message.FriendMessageEvent event){
        super(new Friend(event.getSender().getId()));
        friend = event.getSender();
        chain = new Chain(event.getMessage());
        reply = new QuoteReply(event.getSource());
    }

    public String getMessage() {
        return getChain().getMessage();
    }

    public Message reply(String msg){
        return new Message(friend.sendMessage(reply.plus(msg)));
    }

    public Chain getChain() {
        return chain;
    }

    public int getMessageId() { return reply.getSource().getId(); }
}
