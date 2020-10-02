package site.liangbai.lrobot.event.group;

import net.mamoe.mirai.message.data.QuoteReply;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.contact.GroupMember;
import site.liangbai.lrobot.contact.Message;
import site.liangbai.lrobot.event.HandlerListType;
import site.liangbai.lrobot.message.Chain;
import site.liangbai.lrobot.permission.GroupPermission;
import site.liangbai.lrobot.plugin.PluginManager;
import site.liangbai.lrobot.plugin.annotation.EventObject;

@EventObject(handler = HandlerListType.SUPER)
public class GroupMessageEvent extends GroupEvent {
    private final GroupMember sender;
    private final GroupPermission permission;
    private final Chain chain;
    private final QuoteReply reply;
    private final net.mamoe.mirai.contact.Group group;


    public GroupMessageEvent(net.mamoe.mirai.message.GroupMessageEvent event){
        super(new Group(event.getGroup().getId()));
        sender = getGroup().getGroupMember(event.getSender().getId());
        permission = sender.getPermission();
        reply = new QuoteReply(event.getSource());
        chain = new Chain(event.getMessage());
        group = event.getGroup();
    }

    public GroupPermission getPermission() {
        return permission;
    }

    public GroupMember getSender() {
        return sender;
    }

    public String getMessage() {
        return getChain().getMessage();
    }

    public Message reply(String msg){

        return new Message(group.sendMessage(reply.plus(msg)));
    }

    public void recall(){
        PluginManager.getBot().recall(reply.getSource());
    }

    public void recallLater(long mills){
        PluginManager.getBot().recallIn(reply.getSource(), mills);
    }

    public Chain getChain() {
        return chain;
    }

    public int getMessageId() { return reply.getSource().getId(); }
}
