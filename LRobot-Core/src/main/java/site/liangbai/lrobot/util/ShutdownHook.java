package site.liangbai.lrobot.util;

import site.liangbai.lrobot.plugin.PluginManager;

/**
 * 程序关闭的钩子. 如{@link site.liangbai.lrobot.command.defaults.StopCommand}的System.exit(0)将调用它.
 */
public final class ShutdownHook extends Thread {
    @Override
    public void run() {
        PluginManager.quit();
    }
}
