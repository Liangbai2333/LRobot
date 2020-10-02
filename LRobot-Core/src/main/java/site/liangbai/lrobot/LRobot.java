package site.liangbai.lrobot;

import org.fusesource.jansi.AnsiConsole;
import site.liangbai.lrobot.command.CommandMap;
import site.liangbai.lrobot.command.ConsoleCommandSender;
import site.liangbai.lrobot.command.SimpleCommandMap;
import site.liangbai.lrobot.contact.Message;
import site.liangbai.lrobot.plugin.PluginManager;
import site.liangbai.lrobot.scheduler.impl.CraftScheduler;
import site.liangbai.lrobot.util.JlineReaderThread;
import site.liangbai.lrobot.util.ShutdownHook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public final class LRobot implements ConsoleCommandSender {
    private static final CraftScheduler scheduler = new CraftScheduler();
    private static final CommandMap commandMap = new SimpleCommandMap();
    private static JlineReaderThread jlineThread;
    public static final LRobot INSTANCE = new LRobot();

    static {
        try {
            jlineThread = new JlineReaderThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        installAnsiConsole();
        System.out.println("Loading libraries, please wait...");
        File dependents = new File("libraries");
        if(!dependents.exists()){
            dependents.mkdirs();
        }
        loadDependents(dependents);
        File pluginsPath = new File("plugins");
        if(!pluginsPath.exists()){
            pluginsPath.mkdirs();
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        new Thread(jlineThread).start();
        PluginManager.start();
    }

    public static void loadDependents(File path) {
        path.listFiles(file -> {
            if(!file.isDirectory() && file.getName().toLowerCase().endsWith(".jar")){
                Method method = null;
                try {
                    method = URLClassLoader.class
                            .getDeclaredMethod("addURL", URL.class);
                } catch (NoSuchMethodException | SecurityException e1) {
                    e1.printStackTrace();
                }
                boolean accessible = method.isAccessible();
                try {
                    method.setAccessible(true);
                    URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                    URL url = file.toURI().toURL();
                    method.invoke(classLoader, url);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    method.setAccessible(accessible);
                }
            }
            return false;
        });
    }

    public static void installAnsiConsole() {
        AnsiConsole.systemInstall();
    }
    
    public static void uninstallAnsiConsole() {
        AnsiConsole.systemUninstall();
    }

    public static CraftScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public Message sendMessage(String msg) {
        PluginManager.logger.info(msg);
        return null;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    public static CommandMap getCommandMap() {
        return commandMap;
    }
}
