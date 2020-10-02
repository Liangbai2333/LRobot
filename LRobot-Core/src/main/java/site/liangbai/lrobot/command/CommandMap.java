package site.liangbai.lrobot.command;

import java.util.Collection;

/**
 * 指令的注册、触发的基本接口. 基本实现可查看{@link SimpleCommandMap}.
 */
public interface CommandMap {
    /**
     * 注册一个集合中的所有指令.
     * @param commands 指令集合.
     */
    void registerAll(Collection<Command> commands);

    /**
     * 注册一个指令.
     * @param command 指令.
     * @return 是否注册成功.
     */
    boolean register(Command command);

    /**
     * 执行一个指令.
     * @param sender 指令触发者.
     * @param id 指令触发者的标识符.
     * @param command 指令.
     * @return 是否执行成功.
     */
    boolean dispatch(CommandSender sender, long id, String command);

    /**
     * 清空此CommandMap中的所有指令
     */
    void clearCommands();

    /**
     * 获取一条指令的实现.
     * @param name 指令名称.
     * @return 指令.
     */
    Command getCommand(String name);
}
