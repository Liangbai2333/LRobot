package site.liangbai.lrobot.command;

import org.apache.commons.lang3.Validate;
import site.liangbai.lrobot.plugin.Plugin;

/**
 * 一个插件指令接口的基本实现.
 */
public final class PluginCommand extends Command {
    private final Plugin owningPlugin;
    private CommandExecutor executor;

    /**
     * 插件指令的唯一构造方式.
     * @param name 指令名称.
     * @param owningPlugin 插件.
     */
    public PluginCommand(String name, Plugin owningPlugin) {
        super(name);
        this.owningPlugin = owningPlugin;
    }

    @Override
    public boolean execute(CommandSender sender, long id, String[] args) {
        return executor.execute(sender, this, id, args);
    }

    /**
     * 获取指令的执行者.
     * @return 指令执行者.
     */
    public CommandExecutor getExecutor() {
        return executor;
    }

    /**
     * 获取此指令的插件.
     * @return 插件.
     */
    public Plugin getPlugin() {
        return owningPlugin;
    }

    /**
     * 设置此指令的执行者.
     * @param executor 指令执行者.
     */
    public void setExecutor(CommandExecutor executor) {
        Validate.notNull(executor, "CommandExecutor can not be null");
        this.executor = executor;
    }

    @Override
    public String toString() {
        return "PluginCommand{" +
                "owningPlugin=" + owningPlugin +
                ", executor=" + executor +
                '}';
    }
}
