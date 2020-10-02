package site.liangbai.lrobot.contact.share.templates;

import site.liangbai.lrobot.contact.share.ShareTemplates;

public class LightTemplates extends ShareTemplates {
    public LightTemplates() {
        setName("light");
        setAppId(1105414497);
    }

    @Override
    public synchronized ShareTemplates fromShare(String title, String tag, String desc, String jumpUrl, String imageUrl) {
        setTag(tag);
        setDesc(desc);
        setJumpUrl(jumpUrl);
        setPicUrl(imageUrl);
        setTitle(title);
        return this;
    }
}
