package site.liangbai.lrobot.contact.share;

import com.google.gson.Gson;
import net.mamoe.mirai.message.data.LightApp;
import net.mamoe.mirai.message.data.MessageUtils;
import site.liangbai.lrobot.message.Chain;

import java.util.HashMap;
import java.util.Map;

public abstract class ShareTemplates {
    private final Map<String, Object> map = new HashMap<>();
    private final Map<String, Object> news = new HashMap<>();
    private final Gson gson = new Gson();
    private String name = "";

    public ShareTemplates() {
        map.put("app", "com.tencent.structmsg");
        Map<String, Object> config = new HashMap<>();
        config.put("autosize", true);
        config.put("ctime", System.currentTimeMillis());
        config.put("forward", true);
        config.put("token", "9f98b353519f35dc8c0a26bcecdb1da2");
        config.put("type", "normal");
        map.put("config", config);
        map.put("desc", "新闻");
        Map<String, Object> meta = new HashMap<>();
        news.put("action", "");
        news.put("android_pkg_name", "");
        news.put("app_type", 1);
        news.put("source_icon", "");
        news.put("source_url", "");
        meta.put("news", news);
        map.put("meta", meta);
        map.put("ver", "0.0.0.1");
        map.put("view", "news");
    }

    public void setAppId(int appId) {
        this.news.put("appid", appId);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setTag(String tag) {
        this.news.put("tag", tag);
    }

    public void setTitle(String title) {
        this.news.put("title", title);
        this.map.put("prompt", "[分享]" + title);
    }

    public void setDesc(String desc) {
        this.news.put("desc", desc);
    }

    public void setJumpUrl(String jumpUrl) {
        this.news.put("jumpUrl", jumpUrl);
    }

    public void setPicUrl(String picUrl) {
        this.news.put("preview", picUrl);
    }

    public String toJson() {
        return gson.toJson(getMap());
    }

    public Chain toChain() {
        return new Chain(MessageUtils.newChain(new LightApp(toJson())));
    }

    @Override
    public String toString() {
        return toJson();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public synchronized ShareTemplates fromShare(String title, String tag, String desc, String jumpUrl, String imageUrl) {
        return this;
    }
}
