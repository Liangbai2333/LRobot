package site.liangbai.lrobot.command;

/**
 * 一般指令的父类, 插件指令请使用{@link PluginCommand}.
 */
public abstract class Command {
    private final String name;
    private String description;
    private String usage;
    private CommandMap commandMap = null;


    public Command(String name) {
        this(name, "", "/" + name);
    }

    public Command(String name, String description, String usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    /**
     * 指令被执行时调用.
     * @param sender 消息对象.
     * @param id 消息对象的标识ID, 如CONSOLE为0, 群成员为群成员的QQ账号.
     * @param args 指令参数.
     * @return 返回是否执行成功.
     */
    public abstract boolean execute(CommandSender sender, long id, String[] args);

    /**
     * 返回指令的名称
     * @return 指令名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 注册指令时自动调用.
     * @param commandMap 注册指令的commandMap.
     * @return 是否注册成功.
     */
    public boolean register(CommandMap commandMap) {
        if(allowChangesFrom(commandMap)) {
            this.commandMap = commandMap;
            return true;
        }
        return false;
    }

    /**
     * 注销指令时调用.
     * @param commandMap 指定的commandMap.
     * @return 是否注销成功.
     */
    public boolean unregister(CommandMap commandMap) {
        if(allowChangesFrom(commandMap)) {
            this.commandMap = null;
            return true;
        }
        return false;
    }

    /**
     * 检测是否允许改变commandMap.
     * @param commandMap 指定的commandMap.
     * @return 是否允许改变.
     */
    private boolean allowChangesFrom(CommandMap commandMap) {
        return (this.commandMap == null || this.commandMap == commandMap);
    }

    /**
     * 返回是否已注册commandMap.
     * @return 是否已注册commandMap.
     */
    public boolean isRegistered() {
        return this.commandMap != null;
    }

    /**
     * 获取指令的介绍.
     * @return 指令介绍.
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取指令的使用方法.
     * @return 指令的Usage.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * 设置指令的介绍.
     * @param description 指令介绍.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 设置指令的使用方式.
     * @param usage 指定Usage.
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", usage='" + usage + '\'' +
                ", commandMap=" + commandMap +
                '}';
    }
}
