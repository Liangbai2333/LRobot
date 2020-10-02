package site.liangbai.lrobot.contact.share;

import site.liangbai.lrobot.contact.share.templates.LightTemplates;

import java.util.HashMap;
import java.util.Map;

public class ShareType {
    private static final Map<String, ShareTemplates> templates = new HashMap<>();

    static {
        templates.put("light", new LightTemplates());
    }

    public static void registerHandler(ShareTemplates templates) {
        ShareType.templates.put(templates.getName().toLowerCase(), templates);
    }

    public static ShareTemplates fromName(String name) {
        if(!templates.containsKey(name)) {
            return null;
        }
        return templates.get(name.toLowerCase());
    }
}
