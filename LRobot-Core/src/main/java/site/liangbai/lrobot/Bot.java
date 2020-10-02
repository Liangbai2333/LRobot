package site.liangbai.lrobot;

import site.liangbai.lrobot.contact.Friend;
import site.liangbai.lrobot.contact.Group;
import site.liangbai.lrobot.plugin.Plugin;
import site.liangbai.lrobot.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public final class Bot {
    public static boolean isOnline() {
        return PluginManager.getBot().isOnline();
    }

    public static List<Friend> getFriends(){
        List<Friend> friends = new ArrayList<>();
        PluginManager.getBot().getFriends().forEach(friend -> {
            friends.add(new Friend(friend.getId()));
        });
        return friends;
    }

    public static List<Group> getGroups(){
        List<Group> groups = new ArrayList<>();
        PluginManager.getBot().getGroups().forEach(group -> {
            groups.add(new Group(group.getId()));
        });
        return groups;
    }

    public static Friend getFriend(long friendqqid){
        return new Friend(friendqqid);
    }

    public static Group getGroup(long groupid){
        return new Group(groupid);
    }

    public static List<Plugin> getPlugins(){
        return PluginManager.getPlugins();
    }

    public static Plugin getPlugin(String name){
        for(Plugin plugin : PluginManager.getPlugins()){
            if(plugin.getDescription().getName().equals(name)){
                return plugin;
            }
        }
        return null;
    }

    public static long getBotId(){
        return PluginManager.getBot().getId();
    }

    public static String getName(){
        return PluginManager.getBot().getNick();
    }
}
