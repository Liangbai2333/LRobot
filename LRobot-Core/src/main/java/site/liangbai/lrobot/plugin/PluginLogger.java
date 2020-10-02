package site.liangbai.lrobot.plugin;

import net.mamoe.mirai.utils.SimpleLogger;

public class PluginLogger {
    private String prefix;

    public PluginLogger(Plugin plugin) {
        prefix = plugin.getDescription().getName();
    }

    public void info(String msg){
        PluginManager.logger.printLog(prefix, msg, SimpleLogger.LogPriority.INFO);
    }
    public void debug(String msg){
        PluginManager.logger.printLog(prefix, msg, SimpleLogger.LogPriority.DEBUG);
    }
    public void error(String msg){
        PluginManager.logger.printLog(prefix, msg, SimpleLogger.LogPriority.ERROR);
    }
    public void error(Throwable throwable) { PluginManager.logger.error(throwable); }
    public void error(String msg, Throwable throwable) { PluginManager.logger.error(msg, throwable); }
    public void warning(String msg){
        PluginManager.logger.printLog(prefix, msg, SimpleLogger.LogPriority.WARNING);
    }
    public void verbose(String msg){
        PluginManager.logger.printLog(prefix, msg, SimpleLogger.LogPriority.VERBOSE);
    }
}
