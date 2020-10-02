package site.liangbai.lrobot.contact;

import site.liangbai.lrobot.command.CommandSender;
import site.liangbai.lrobot.message.Chain;

public abstract class User implements CommandSender {
    public abstract String getName();

    public abstract String getAvatarUrl();

    public abstract long getId();

    public abstract Message sendMessage(String msg);

    protected abstract Message sendMessage(Chain chain);
}
