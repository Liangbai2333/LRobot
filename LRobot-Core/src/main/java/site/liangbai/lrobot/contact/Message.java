package site.liangbai.lrobot.contact;

import net.mamoe.mirai.message.MessageReceipt;
import site.liangbai.lrobot.message.Chain;

public class Message {
    private MessageReceipt message;
    private Chain chain;

    public Message(MessageReceipt message){
        this.message = message;
        this.chain = new Chain(message.getSource().getOriginalMessage());
    }

    public int getMessageId() { return message.getSource().getId(); }

    public void recall(){
        message.recall();
    }

    public void recallLater(long mills){
        message.recallIn(mills);
    }

    public Message reply(String msg){
        return new Message(message.quoteReply(LR.parseMiraiCode(msg).getChain()));
    }

    public Chain getChain() {
        return chain;
    }

    public String getMessage() {
        return chain.getMessage();
    }
}
