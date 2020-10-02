package site.liangbai.lrobot.command;

/**
 * 指令执行者.
 */
public interface CommandExecutor {
    /**
     * 指令被执行时调用.
     * @param sender 消息对象.
     * @param id 消息对象的标识ID, 如CONSOLE为0, 群成员为群成员的QQ账号.
     * @param args 指令参数.
     * @return 返回是否执行成功.
     */
    boolean execute(CommandSender sender, Command command, long id, String[] args);
}
