package site.liangbai.lrobot.plugin.java;

import kotlin.text.Charsets;
import net.mamoe.mirai.Bot;
import site.liangbai.lrobot.command.Command;
import site.liangbai.lrobot.command.CommandSender;
import site.liangbai.lrobot.configuration.InvalidConfigurationException;
import site.liangbai.lrobot.configuration.file.FileConfiguration;
import site.liangbai.lrobot.configuration.file.YamlConfiguration;
import site.liangbai.lrobot.plugin.PluginBase;
import site.liangbai.lrobot.plugin.PluginDescription;
import site.liangbai.lrobot.plugin.PluginLoader;
import site.liangbai.lrobot.plugin.PluginLogger;
import site.liangbai.lrobot.plugin.exception.InvalidPluginException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import com.google.common.io.ByteStreams;

public abstract class JavaPlugin extends PluginBase {
    private boolean isEnabled = false;
    private PluginLoader loader = null;
    private File file = null;
    private PluginDescription description = null;
    private File dataFolder = null;
    private ClassLoader classLoader = null;
    private FileConfiguration newConfig = null;
    private File configFile = null;
    private PluginLogger logger = null;

    public JavaPlugin() {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        if (!(classLoader instanceof PluginClassLoader)) {
            throw new IllegalStateException("JavaPlugin requires " + PluginClassLoader.class.getName());
        }
    }

    @Override
    public final File getDataFolder() {
        return dataFolder;
    }

    @Override
    public final PluginLoader getPluginLoader() {
        return loader;
    }

    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }

    protected File getFile() {
        return file;
    }

    @Override
    public final PluginDescription getDescription() {
        return description;
    }

    @Override
    public FileConfiguration getConfig() {
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }

    @SuppressWarnings("deprecation")
    protected final Reader getTextResource(String file) {
        final InputStream in = getResource(file);

        return in == null ? null : new InputStreamReader(in, FileConfiguration.UTF8_OVERRIDE ? Charsets.UTF_8 : Charset.defaultCharset());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void reloadConfig() {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        final YamlConfiguration defConfig;
        if (FileConfiguration.UTF8_OVERRIDE) {
            defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
        } else {
            final byte[] contents;
            defConfig = new YamlConfiguration();
            try {
                contents = ByteStreams.toByteArray(defConfigStream);
            } catch (final IOException e) {
                throw new InvalidPluginException("Unexpected failure reading config.yml", e);
            }

            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8))) {
                getLogger().warning("Default system encoding may have misread config.yml from plugin jar");
            }

            try {
                defConfig.loadFromString(text);
            } catch (final InvalidConfigurationException e) {
                throw new InvalidPluginException("Cannot load configuration from jar", e);
            }
        }

        newConfig.setDefaults(defConfig);
    }

    @Override
    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            throw new InvalidPluginException("Could not save config to " + configFile, ex);
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + file);
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.warning("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            throw new InvalidPluginException("Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    @Override
    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    protected final ClassLoader getClassLoader() {
        return classLoader;
    }

    public final void setEnabled(final boolean enabled) {
        this.isEnabled = enabled;
    }

    final void init(PluginLoader loader, PluginDescription description, File dataFolder, File file, ClassLoader classLoader) {
        this.loader = loader;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.classLoader = classLoader;
        this.configFile = new File(dataFolder, "config.yml");
        this.logger = new PluginLogger(this);
    }

    @Deprecated
    public final boolean isInitialized() {
        return true;
    }

    @Override
    public void onLoad() {}

    @Override
    public void onDisable() {}

    @Override
    public void onEnable() {}

    @Override
    public final PluginLogger getLogger() {
        return logger;
    }

    @Override
    public String toString() {
        return description.getName();
    }
}
