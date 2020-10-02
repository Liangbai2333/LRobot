package site.liangbai.lrobot.command;

import site.liangbai.lrobot.LRobot;
import site.liangbai.lrobot.command.defaults.StopCommand;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * {@link CommandMap}的基本实现. 注册可查看{@link LRobot#getCommandMap()}
 */
public class SimpleCommandMap implements CommandMap {
    /**
     * 指令参数分割的正则表达式.
     */
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
    /**
     * 已注册指令存放的Map.
     */
    protected final Map<String, Command> knownCommands = new HashMap<>();

    public SimpleCommandMap() {
        setDefaultCommands();
    }

    /**
     * 注册基本指令.
     */
    private void setDefaultCommands() {
        register(new StopCommand());
    }

    @Override
    public void registerAll(Collection<Command> commands) {
        commands.forEach(this::register);
    }

    @Override
    public synchronized boolean register(Command command) {
        if(knownCommands.containsKey(command.getName().toLowerCase())) {
            return false;
        }
        knownCommands.put(command.getName().toLowerCase(), command);
        command.register(this);
        return true;
    }

    @Override
    public boolean dispatch(CommandSender sender, long id, String command) {
        String[] args = PATTERN_ON_SPACE.split(command);
        if(args.length == 0) {
            return false;
        }

        Command target = getCommand(args[0]);

        if(target == null) {
            if(sender instanceof ConsoleCommandSender) sender.sendMessage("Unknown command. Type \"/help\" for help.");
            return false;
        }

        return target.execute(sender, id, args);
    }

    @Override
    public void clearCommands() {
        knownCommands.forEach((key, value) -> {
            value.unregister(this);
        });
        knownCommands.clear();
        setDefaultCommands();
    }

    @Override
    public Command getCommand(String name) {
        return knownCommands.get(name.toLowerCase());
    }
}
