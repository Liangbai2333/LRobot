package site.liangbai.lrobot.plugin;

import site.liangbai.lrobot.event.Event;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public interface PluginLoader {

    Plugin loadPlugin(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    void enablePlugin(Plugin plugin);

    void disablePlugin(Plugin plugin);

    Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Object listener, final Plugin plugin, final boolean isStatic);
}
