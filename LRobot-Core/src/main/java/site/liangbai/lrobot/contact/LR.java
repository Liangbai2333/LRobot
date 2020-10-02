package site.liangbai.lrobot.contact;

import com.google.gson.Gson;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import site.liangbai.lrobot.contact.message.ServiceMessageType;
import site.liangbai.lrobot.contact.music.MusicType;
import site.liangbai.lrobot.contact.share.ShareType;
import site.liangbai.lrobot.message.Chain;
import site.liangbai.lrobot.plugin.PluginManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public final class LR {
    public static String at(GroupMember member) {
        return String.format("[mirai:at:%d,%s]", member.getId(), "@" + member.getNameCard());
    }

    public static String at(GroupMember...members) {
        StringBuilder sb = new StringBuilder();
        for(GroupMember member : members) {
            sb.append(String.format("[mirai:at:%d,%s]", member.getId(), "@" + member.getNameCard()));
        }
        return sb.toString();
    }

    public static String atAll() {
        return "[mirai:atall]";
    }

    public static String face(int id) {
        return String.format("[mirai:face:%d]", id);
    }

    public static String face(int...id) {
        StringBuilder sb = new StringBuilder();
        for(int faceId : id) {
            sb.append(String.format("[mirai:face:%d]", faceId));
        }
        return sb.toString();
    }

    public static String image(File image) {
        Image temp = PluginManager.getBot().getSelfQQ().uploadImage(image);
        return String.format("[mirai:image:%s]", temp.getImageId());
    }

    public static String image(URL image) {
        Image temp = PluginManager.getBot().getSelfQQ().uploadImage(image);
        return String.format("[mirai:image:%s]", temp.getImageId());
    }

    public static String image(InputStream image) {
        Image temp = PluginManager.getBot().getSelfQQ().uploadImage(image);
        return String.format("[mirai:image:%s]", temp.getImageId());
    }

    public static String image(BufferedImage image) {
        Image temp = PluginManager.getBot().getSelfQQ().uploadImage(image);
        return String.format("[mirai:image:%s]", temp.getImageId());
    }

    @Deprecated
    public static String image(String imageId) {
        return String.format("[mirai:image:%s]", imageId);
    }

    public static String poke(String name, int type, int id) {
        return String.format("[mirai:poke:%s,%d,%d]", name, type, id);
    }

    public static String groupFlashImage(Group group, File image) {
        Image temp = PluginManager.getBot().getGroup(group.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    public static String groupFlashImage(Group group, URL image) {
        Image temp = PluginManager.getBot().getGroup(group.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    public static String groupFlashImage(Group group, InputStream image) {
        Image temp = PluginManager.getBot().getGroup(group.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    public static String groupFlashImage(Group group, BufferedImage image) {
        Image temp = PluginManager.getBot().getGroup(group.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    public static String friendFlashImage(Friend friend, File image) {
        Image temp = PluginManager.getBot().getFriend(friend.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    public static String friendFlashImage(Friend friend, URL image) {
        Image temp = PluginManager.getBot().getFriend(friend.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    public static String friendFlashImage(Friend friend, InputStream image) {
        Image temp = PluginManager.getBot().getFriend(friend.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    public static String friendFlashImage(Friend friend, BufferedImage image) {
        Image temp = PluginManager.getBot().getFriend(friend.getId()).uploadImage(image);
        return String.format("[mirai:flash:%s]", temp.getImageId());
    }

    @Deprecated
    public static String flashImage(String imageId) {
        return String.format("[mirai:flash:%s]", imageId);
    }

    public static Message sendLightApp(User user, Map<String, Object> data) {
        Gson gson = new Gson();
        return user.sendMessage(new Chain(MessageUtils.newChain(new LightApp(gson.toJson(data)))));
    }

    public static Message sendLightApp(User user, String data) {
        Gson gson = new Gson();
        return user.sendMessage(new Chain(MessageUtils.newChain(new LightApp(data))));
    }

    public static Message sendServiceMessage(User user, int serviceId, String data) {
        return user.sendMessage(new Chain(MessageUtils.newChain(new ServiceMessage(serviceId, data))));
    }

    public static Message sendServiceMessage(User user, ServiceMessageType messageType, String data) {
        return user.sendMessage(new Chain(MessageUtils.newChain(new ServiceMessage(messageType.getServiceId(), data))));
    }

    public static Message sendMusic(User user, String type, Object id) {
        if(type.equalsIgnoreCase("163")) {
            return user.sendMessage(MusicType.fromName("163").fromObject(id).toChain());
        }
        if(type.equalsIgnoreCase("kugou")) {
            return user.sendMessage(MusicType.fromName("kugou").fromObject(id).toChain());
        }
        return null;
    }

    public static Message sendShare(User user, String title, String tag, String desc, String jumpUrl, String imageUrl) {
        return user.sendMessage(ShareType.fromName("light").fromShare(title, tag, desc, jumpUrl, imageUrl).toChain());
    }

    protected static Chain parseMiraiCode(String msg) {
        return new Chain(MiraiCode.parseMiraiCode(msg));
    }

}
