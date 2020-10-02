package site.liangbai.lrobot.command;

import site.liangbai.lrobot.contact.Message;

/**
 * 指令触发者的接口.
 */
public interface CommandSender {
    /**
     * 向这个指令触发者发送消息.
     * @param msg 消息内容.
     * @return 消息对象, 如果触发者为{@link ConsoleCommandSender}则为null.
     */
    Message sendMessage(String msg);

    /**
     * 获取这个指令触发者的名称.
     * @return 触发者名.
     */
    String getName();
}
