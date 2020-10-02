package site.liangbai.lrobot.contact;

import site.liangbai.lrobot.message.Chain;
import site.liangbai.lrobot.plugin.PluginManager;

public class Friend extends User {
    private final net.mamoe.mirai.contact.Friend friend;

    public Friend(long id){
        friend = PluginManager.getBot().getFriend(id);
    }

    public long getId(){
        return friend.getId();
    }

    public String getName(){
        return friend.getNick();
    }

    public String getAvatarUrl(){
        return friend.getAvatarUrl();
    }

    public Message sendMessage(String msg){
        return sendMessage(LR.parseMiraiCode(msg));
    }

    protected Message sendMessage(Chain chain) {
        return new Message(friend.sendMessage(chain.getChain()));
    }
}
