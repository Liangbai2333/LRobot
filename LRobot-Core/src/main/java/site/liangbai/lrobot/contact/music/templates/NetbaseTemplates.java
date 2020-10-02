package site.liangbai.lrobot.contact.music.templates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import site.liangbai.lrobot.contact.music.MusicTemplates;
import site.liangbai.lrobot.util.network.HttpRequest;
import site.liangbai.lrobot.util.network.UserAgent;

import java.io.IOException;
import java.util.Random;

/**
 * 网易云音乐模板的基本实现.
 * @author Liangbai
 */
public final class NetbaseTemplates extends MusicTemplates {

    public NetbaseTemplates() {
        setName("163");
        setTag("网易云音乐");
        setAppId(100495085);
    }

    @Override
    public synchronized MusicTemplates fromObject(Object object) {
        long id = Long.parseLong(String.valueOf(object));
        HttpRequest request;
        String musicResult = null;
        try {
            request = new HttpRequest("http://music.163.com/api/song/detail/?id=" + id + "&ids=%5B" + id + "%5D");
            request.putRequestProperty("User-Agent", UserAgent.LINUX.getUserAgent());
            musicResult = request.sendRequestAndReturn();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject musicJson = new JsonParser().parse(musicResult).getAsJsonObject().get("songs").getAsJsonArray().get(0).getAsJsonObject();
        String name = musicJson.get("name").getAsString();
        long musicId = musicJson.get("id").getAsLong();
        JsonArray musicAr = musicJson.get("artists").getAsJsonArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < musicAr.size(); i++) {
            if(i + 1 == musicAr.size()) {
                sb.append(musicAr.get(i).getAsJsonObject().get("name").getAsString());
            } else {
                sb.append(musicAr.get(i).getAsJsonObject().get("name").getAsString()).append("/");
            }
        }
        String picUrl = musicJson.get("album").getAsJsonObject().get("picUrl").getAsString();
        String jumpUrl = String.format("https://y.music.163.com/m/song/%d", musicId);
        String musicUrl = String.format("http://music.163.com/song/media/outer/url?id=%d", musicId);
        setDesc(sb.toString());
        setJumpUrl(jumpUrl);
        setMusicUrl(musicUrl);
        setPicUrl(picUrl);
        setTitle(name);
        return this;
    }
}
