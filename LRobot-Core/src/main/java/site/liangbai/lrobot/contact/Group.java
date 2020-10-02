package site.liangbai.lrobot.contact;
import site.liangbai.lrobot.message.Chain;
import site.liangbai.lrobot.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class Group extends User {
    private final net.mamoe.mirai.contact.Group group;

    public Group(long id){
        group = PluginManager.getBot().getGroup(id);
    }

    public Message sendMessage(String msg){
        return sendMessage(LR.parseMiraiCode(msg));
    }

    protected Message sendMessage(Chain chain) {
        return new Message(group.sendMessage(chain.getChain()));
    }

    public long getId(){
        return group.getId();
    }

    public GroupMember getGroupMember(long qqid){
        return new GroupMember(getId(), qqid);
    }

    public List<GroupMember> getGroupMembers(){
        List<GroupMember> members = new ArrayList<>();
        group.getMembers().forEach(member -> {
            members.add(new GroupMember(getId(), member.getId()));
        });
        return members;
    }

    public GroupMember getBotAsMember(){
        return getGroupMember(group.getBotAsMember().getId());
    }

    public String getName(){
        return group.getName();
    }

    public void setName(String name) { group.setName(name); }

    public boolean quit(){
        return group.quit();
    }

    public String getAvatarUrl(){
        return group.getAvatarUrl();
    }

    public GroupSettings getGroupSettings(){
        return new GroupSettings(getId());
    }

    public GroupMember getOwner(){
        return getGroupMember(group.getOwner().getId());
    }
}
