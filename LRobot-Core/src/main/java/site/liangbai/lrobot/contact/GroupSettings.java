package site.liangbai.lrobot.contact;

import site.liangbai.lrobot.plugin.PluginManager;

public class GroupSettings {
    private final net.mamoe.mirai.contact.GroupSettings settings;

    public GroupSettings(long id){
        this.settings = PluginManager.getBot().getGroup(id).getSettings();
    }

    public String getEntranceAnnouncement(){
        return settings.getEntranceAnnouncement();
    }

    public boolean isAllowMemberInvite(){
        return settings.isAllowMemberInvite();
    }

    public boolean isAnonymousChatEnabled(){
        return settings.isAnonymousChatEnabled();
    }

    public boolean isAutoApproveEnabled(){
        return settings.isAutoApproveEnabled();
    }

    public boolean isMuteAll(){
        return settings.isMuteAll();
    }

    public void setMuteAll(boolean setornot){
        settings.setMuteAll(setornot);
    }

    public void setAllowMemberInvite(boolean setornot){
        settings.setAllowMemberInvite(setornot);
    }

    public void setEntranceAnnouncement(String text){
        settings.setEntranceAnnouncement(text);
    }
}
