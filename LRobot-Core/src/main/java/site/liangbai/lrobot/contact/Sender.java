package site.liangbai.lrobot.contact;

import site.liangbai.lrobot.message.Chain;

public class Sender extends User {
    private final net.mamoe.mirai.contact.User user;

    public Sender(net.mamoe.mirai.contact.User user){
        this.user = user;
    }

    public String getName() {
        return user.getNick();
    }

    public String getAvatarUrl() {
        return user.getAvatarUrl();
    }

    public long getId() {
        return user.getId();
    }

    public Message sendMessage(String msg) {
        return sendMessage(LR.parseMiraiCode(msg));
    }

    protected Message sendMessage(Chain chain) {
        return new Message(user.sendMessage(chain.getChain()));
    }
}
