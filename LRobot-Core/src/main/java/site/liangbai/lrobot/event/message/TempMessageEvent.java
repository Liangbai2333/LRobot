package site.liangbai.lrobot.event.message;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.QuoteReply;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.contact.Message;
import site.liangbai.lrobot.event.Event;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.message.Chain;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class TempMessageEvent extends Event {
    private final GroupMember sender;
    private final QuoteReply reply;
    private final Member member;
    private final Chain chain;

    public TempMessageEvent(net.mamoe.mirai.message.TempMessageEvent event){
        this.sender = new GroupMember(event.getGroup().getId(), event.getSender().getId());
        reply = new QuoteReply(event.getSource());
        member = event.getSender();
        chain = new Chain(event.getMessage());
    }

    public GroupMember getSender() {
        return sender;
    }

    public String getMessage() {
        return getChain().getMessage();
    }

    public Group getGroup(){
        return sender.getGroup();
    }

    public Message reply(String msg){
        return new Message(member.sendMessage(reply.plus(msg)));
    }

    public Chain getChain() {
        return chain;
    }
}
