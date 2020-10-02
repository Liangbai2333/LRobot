package site.liangbai.lrobot.contact.music;

import site.liangbai.lrobot.contact.music.templates.KugouTemplates;
import site.liangbai.lrobot.contact.music.templates.NetbaseTemplates;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放音乐模板的类.
 * @author Liangbai
 */
public final class MusicType {
    private static final Map<String, MusicTemplates> templates = new HashMap<>();

    static {
        registerHandler(new KugouTemplates());
        registerHandler(new NetbaseTemplates());
    }

    /**
     * 注册一个音乐模板对象.
     * @param templates 模板对象.
     */
    public static void registerHandler(MusicTemplates templates) {
        MusicType.templates.put(templates.getName().toLowerCase(), templates);
    }

    /**
     * 使用名称获取一个音乐模板对象.
     * @param name 模板名称
     * @return 音乐模板.
     */
    public static MusicTemplates fromName(String name) {
        if(!templates.containsKey(name)) {
            return null;
        }
        return templates.get(name.toLowerCase());
    }
}
