package site.liangbai.lrobot.plugin;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.Utils;
import org.apache.commons.lang3.Validate;
import site.liangbai.lrobot.LRobot;
import site.liangbai.lrobot.event.AlwaysListenerKt;
import site.liangbai.lrobot.event.EventUtils;
import site.liangbai.lrobot.event.service.PluginAllLoadedEvent;
import site.liangbai.lrobot.event.service.PluginLoadEvent;
import site.liangbai.lrobot.event.service.ServerEnableEvent;
import site.liangbai.lrobot.plugin.java.JavaPluginLoader;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PluginManager {
    private static final List<Plugin> plugins = new ArrayList<>();
    private static Bot bot = null;
    public static LRobotLogger logger;
    private static JavaPluginLoader loader;
    private static final ExecutorService threadPool;

    static {
        threadPool = Executors.newCachedThreadPool();
    }

    public static void start(){
        threadPool.submit(() -> {
            threadPool.execute(() -> {
                int ticks = 0;
                while(true) {
                    ++ticks;
                    LRobot.getScheduler().mainThreadHeartbeat(ticks);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            logger = new LRobotLogger("LRobot");
            Utils.setDefaultLogger(s -> logger);
            loader = new JavaPluginLoader(logger);
            File jarpath = new File("plugins");
            if(!jarpath.exists()){
                jarpath.mkdirs();
            }
            for(File file : getAllPlugins(jarpath)){
                logger.info("Load " + file.getName());
                try {
                    Plugin plugin = loader.loadPlugin(file);
                    if(plugin != null) {
                        plugins.add(plugin);
                        PluginLoadEvent event = new PluginLoadEvent(plugin);
                        EventUtils.callEvent(event);
                        logger.info("Loading " + plugin.getDescription().getName() + " Version: " + plugin.getDescription().getVersion() + " Author: " + plugin.getDescription().getAuthor());
                        plugin.onLoad();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            EventUtils.callEvent(new PluginAllLoadedEvent());
        });
    }

    public synchronized static void login() {
        Validate.notNull(bot, "The bot is null, you should create it before this method execute");
        bot.login();
    }

    public static void quit() {
        Validate.notNull(bot, "The bot is null, you should create it before this method execute");
        for(Plugin plugin : plugins){
            loader.disablePlugin(plugin);
        }
        bot.close(new Throwable());
    }

    public synchronized static void login(Bot loginBot) {
        Validate.isTrue(bot == null, "The bot is not null, can not create it again");
        bot = loginBot;
        bot.login();
        AlwaysListenerKt.start(bot);
        for(Plugin plugin : plugins){
            loader.enablePlugin(plugin);
        }
        ServerEnableEvent event = new ServerEnableEvent();
        EventUtils.callEvent(event);
    }

    public static List<File> getAllPlugins(File path){
        List<File> fileList = new ArrayList<>();
        path.listFiles(file -> {
            if(!file.isDirectory() && file.getName().toLowerCase().endsWith(".jar")){
                fileList.add(file);
            }
            return false;
        });
        return fileList;
    }

    public static boolean loadPlugin(Plugin plugin){
        if(plugin != null && plugin.getDescription() != null){
            plugins.add(plugin);
            if(bot.isOnline()){
                loader.enablePlugin(plugin);
            }
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean loadPlugin(File file, boolean runtime){
        Plugin plugin = null;
        try {
            plugin = loader.loadPlugin(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!runtime){
            return plugin != null;
        }
        return loadPlugin(plugin);
    }

    public static Bot getBot() {
        return bot;
    }

    public static List<Plugin> getPlugins() {
        return plugins;
    }

    public static JavaPluginLoader getLoader() {
        return loader;
    }
}
