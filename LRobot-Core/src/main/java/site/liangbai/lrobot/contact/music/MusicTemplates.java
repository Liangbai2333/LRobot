package site.liangbai.lrobot.contact.music;

import com.google.gson.Gson;
import net.mamoe.mirai.message.data.LightApp;
import net.mamoe.mirai.message.data.MessageUtils;
import site.liangbai.lrobot.message.Chain;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个音乐模板的接口.
 * @author Liangbai
 */
public abstract class MusicTemplates {
    private String name = "";
    private final Map<String, Object> map = new HashMap<>();
    private final Map<String, Object> music = new HashMap<>();
    private final Gson gson = new Gson();

    public MusicTemplates() {
        map.put("app", "com.tencent.structmsg");
        Map<String, Object> config = new HashMap<>();
        config.put("autosize", true);
        config.put("ctime", System.currentTimeMillis());
        config.put("forward", true);
        config.put("type", "normal");
        map.put("config", config);
        map.put("desc", "音乐");
        Map<String, Object> meta = new HashMap<>();
        music.put("action", "");
        music.put("android_pkg_name", "");
        music.put("app_type", 1);
        music.put("sourceMsgId", "0");
        music.put("source_icon", "");
        music.put("source_url", "");
        meta.put("music", music);
        map.put("meta", meta);
        map.put("ver", "0.0.0.1");
        map.put("view", "music");
    }

    /**
     * 从一个对象中获取此音乐模板的JSON值, 也就是生成音乐时所用的方法.
     * @param object 指定对象.
     * @return 自身.
     */
    public abstract MusicTemplates fromObject(Object object);

    /**
     * 置这条Json的appId.
     * @param appId appId.
     */
    public void setAppId(int appId) {
        this.music.put("appid", appId);
    }

    /**
     * 获取这条Json消息的Map集合.
     * @return map.
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * 设置这条消息的标签.
     * @param tag 标签.
     */
    public void setTag(String tag) {
        this.music.put("tag", tag);
    }

    public void setTitle(String title) {
        this.music.put("title", title);
        this.map.put("prompt", "[分享]" + title);
    }

    /**
     * 设置这条消息的副标题
     * @param desc 副标题
     */
    public void setDesc(String desc) {
        this.music.put("desc", desc);
    }

    /**
     * 设置这条消息的跳转链接.
     * @param jumpUrl 跳转链接
     */
    public void setJumpUrl(String jumpUrl) {
        this.music.put("jumpUrl", jumpUrl);
    }

    /**
     * 设置图片链接.
     * @param picUrl 图片链接.
     */
    public void setPicUrl(String picUrl) {
        this.music.put("preview", picUrl);
    }

    /**
     * 设置音乐链接.
     * @param musicUrl 音乐链接.
     */
    public void setMusicUrl(String musicUrl) {
        this.music.put("musicUrl", musicUrl);
    }

    /**
     * 将这条消息的map({@link #getMap()})转为Json.
     * @return json.
     */
    public String toJson() {
        return gson.toJson(getMap());
    }

    /**
     * 将这条消息的Json({@link #toJson()})转为Chain对象.
     * @return Chain对象.
     */
    public Chain toChain() {
        return new Chain(MessageUtils.newChain(new LightApp(toJson())));
    }

    /**
     * 返回这条消息的Json形式.
     * @return Json.
     */
    @Override
    public String toString() {
        return toJson();
    }

    /**
     * 设置这个音乐模板的名称.
     * @param name 模板名.
     */
    public void setName(String name) {
       this.name = name;
    }

    /**
     * 获取这个音乐模板的名称
     * @return 模板名称.
     */
    public String getName() {
        return this.name;
    }
}
