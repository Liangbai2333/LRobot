package site.liangbai.lrobot.contact.music.templates;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import site.liangbai.lrobot.contact.music.MusicTemplates;
import site.liangbai.lrobot.util.network.HttpRequest;
import site.liangbai.lrobot.util.network.UserAgent;

import java.io.IOException;
import java.util.Random;

/**
 * 酷狗音乐模板的基本实现.
 * @author Liangbai
 */
public final class KugouTemplates extends MusicTemplates {

    public KugouTemplates() {
        this.setName("kugou");
        this.setTag("酷狗音乐");
        this.setAppId(205141);
    }

    @Override
    public synchronized MusicTemplates fromObject(Object object) {
        String hash = (String) object;
        HttpRequest request;
        String musicResult = null;
        try {
            request = new HttpRequest("http://www.kugou.com/yy/index.php?r=play/getdata&hash=" + hash);
            request.putRequestProperty("User-Agent", UserAgent.LINUX.getUserAgent());
            request.putRequestProperty("Cookie", "kg_mid=" + new Random().nextInt(Integer.MAX_VALUE));
            musicResult = request.sendRequestAndReturn();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject musicJson = new JsonParser().parse(musicResult).getAsJsonObject().get("data").getAsJsonObject();
        String name = musicJson.get("song_name").getAsString();
        String albumName = musicJson.get("album_name").getAsString();
        int albumId = musicJson.get("album_id").getAsInt();
        String authorName = musicJson.get("author_name").getAsString();
        String picUrl = musicJson.get("img").getAsString();
        String jumpUrl = String.format("https://www.kugou.com/song/#hash=%s&album_id=%d", hash, albumId);
        String musicUrl = musicJson.get("play_url").getAsString();
        this.setDesc(authorName + " · " + albumName);
        this.setJumpUrl(jumpUrl);
        this.setMusicUrl(musicUrl);
        this.setPicUrl(picUrl);
        this.setTitle(name);
        return this;
    }
}
