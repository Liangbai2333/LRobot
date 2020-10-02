package site.liangbai.lrobot.command.defaults;

import site.liangbai.lrobot.command.Command;
import site.liangbai.lrobot.command.CommandSender;
import site.liangbai.lrobot.command.ConsoleCommandSender;

/**
 * /stop命令的默认实现.
 */
public class StopCommand extends Command {
    public StopCommand() {
        super("stop");
        setDescription("The command to stop LRobot framework.");
        setUsage("/stop");
    }

    @Override
    public boolean execute(CommandSender sender, long id, String[] args) {
        if(!(sender instanceof ConsoleCommandSender)) return true;
        System.exit(0);
        return true;
    }
}
