package site.liangbai.lrobot.contact;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;
import site.liangbai.lrobot.message.Chain;
import site.liangbai.lrobot.permission.GroupPermission;
import site.liangbai.lrobot.plugin.PluginManager;

public class GroupMember extends User {
    private final Member member;

    public GroupMember(long fromGroup, long id){
        member = PluginManager.getBot().getGroup(fromGroup).getOrNull(id);
    }

    public Message sendMessage(String msg){
        return sendMessage(LR.parseMiraiCode(msg));
    }

    protected Message sendMessage(Chain chain) {
        return new Message(member.sendMessage(chain.getChain()));
    }

    public GroupPermission getPermission(){
        return member.getPermission() == MemberPermission.OWNER ? GroupPermission.OWNER : member.getPermission() == MemberPermission.ADMINISTRATOR ? GroupPermission.ADMINISTRATOR : GroupPermission.MEMBER;
    }

    public void mute(int seconds){
        member.mute(seconds);
    }

    public void kick(){
        member.kick();
    }

    public void kick(String reason){
        member.kick(reason);
    }

    public Group getGroup(){
        return new Group(member.getGroup().getId());
    }

    public String getNameCard(){
        return member.getNameCard();
    }

    public void setNameCard(String nameCard) { member.setNameCard(nameCard); }

    public String getSpecialName(){
        return member.getSpecialTitle();
    }

    public void setSpecialName(String nameCard) { member.setSpecialTitle(nameCard); }

    public String getName(){
        return member.getNick();
    }

    public String getAvatarUrl() {
        return member.getAvatarUrl();
    }

    public long getId() {
        return member.getId();
    }

    public int getMuteTimeRemaining(){
        return member.getMuteTimeRemaining();
    }

    public void unmute(){
        member.unmute();
    }
}
