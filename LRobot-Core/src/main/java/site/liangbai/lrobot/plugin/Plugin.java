package site.liangbai.lrobot.plugin;

import site.liangbai.lrobot.command.CommandExecutor;
import site.liangbai.lrobot.configuration.file.FileConfiguration;

import java.io.File;
import java.io.InputStream;

public interface Plugin {

    File getDataFolder();

    PluginDescription getDescription();

    FileConfiguration getConfig();

    InputStream getResource(String filename);

    void saveConfig();

    void saveDefaultConfig();

    void saveResource(String resourcePath, boolean replace);

    void reloadConfig();

    PluginLoader getPluginLoader();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    void onDisable();

    void onLoad();

    void onEnable();

    PluginLogger getLogger();

    String getName();
}
