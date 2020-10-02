package site.liangbai.lrobot.message;

import net.mamoe.mirai.message.data.MessageChain;

public final class Chain {
    private MessageChain chain;

    public Chain(MessageChain chain){
        this.chain = chain;
    }

    public MessageChain getChain() {
        return chain;
    }

    public String getAllMessage() {
        return chain.toString();
    }

    public String getMessage(){
        String temp = chain.toString();
        return temp.substring(temp.indexOf("]") + 1);
    }

    public String getMessageIgnoreCode() { return chain.contentToString(); }
}
