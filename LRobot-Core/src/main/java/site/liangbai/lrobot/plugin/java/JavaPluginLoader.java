package site.liangbai.lrobot.plugin.java;

import javafx.util.Pair;
import org.apache.commons.lang3.Validate;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import site.liangbai.lrobot.LRobot;
import site.liangbai.lrobot.command.CommandExecutor;
import site.liangbai.lrobot.command.PluginCommand;
import site.liangbai.lrobot.event.*;
import site.liangbai.lrobot.event.service.PluginDisableEvent;
import site.liangbai.lrobot.event.service.PluginEnableEvent;
import site.liangbai.lrobot.plugin.*;
import site.liangbai.lrobot.plugin.annotation.CommandVisitor;
import site.liangbai.lrobot.plugin.annotation.SubscribeEvent;
import site.liangbai.lrobot.plugin.exception.InvalidPluginException;
import site.liangbai.lrobot.plugin.util.PluginCommandInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class JavaPluginLoader implements PluginLoader {
    private final LRobotLogger logger;
    private final Map<Class<? extends Plugin>, Plugin> pluginsMain = new LinkedHashMap<>();
    private final Map<String, Plugin> plugins = new LinkedHashMap<>();
    private final Map<String, PluginClassLoader> pluginsLoader = new LinkedHashMap<>();

    public JavaPluginLoader(LRobotLogger logger) {
        this.logger = logger;
    }

    @Override
    public Plugin loadPlugin(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Validate.notNull(file, "Plugin file can not be null");
        List<Pair<String, PluginDescription>> mainClasses = new ArrayList<>();
        List<String> listenerClasses = new ArrayList<>();
        Map<String, PluginCommandInfo> commandClasses = new HashMap<>();
        URL[] urls = new URL[1];
        urls[0] = file.toURI().toURL();
        PluginClassLoader loader = new PluginClassLoader(urls, getClass().getClassLoader());
        AtomicReference<Plugin> p = new AtomicReference<>();
        getAllClassesInputStreamByFile(file).forEach(value -> {
            try {
                ClassNode cn = readClass(value);
                List<AnnotationNode> annotations = getASMClassAnnotations(cn);
                if(annotations != null) {
                    String name = getASMClassName(cn.name);
                    annotations.forEach(annotation -> {
                        String desc = asmAnnotationDescToClassPath(annotation.desc);
                        if(desc.equals(site.liangbai.lrobot.plugin.annotation.Plugin.class.getName())) {
                            PluginDescription pluginDescription = new PluginDescription(asmAnnotationValuesToMap(annotation.values), name);
                            mainClasses.add(new Pair<>(name, pluginDescription));
                        }
                        if(desc.equals(SubscribeEvent.class.getName())) {
                            listenerClasses.add(name);
                        }
                        if(desc.equals(CommandVisitor.class.getName())) {
                            Map<String, Object> map = asmAnnotationValuesToMap(annotation.values);
                            String commandName = (String) map.get("commandName");
                            String usage = map.containsKey("usage") ? (String) map.get("usage") : "/" + commandName;
                            String description = map.containsKey("description") ? (String) map.get("description") : "";
                            commandClasses.put(name, new PluginCommandInfo(commandName, usage, description));
                        }
                    });
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        if(mainClasses.size() > 0) {
            if(mainClasses.size() > 1) {
                throw new InvalidPluginException("Can't have more than one main class");
            }
            String mainName = mainClasses.get(0).getKey();
            PluginDescription description = mainClasses.get(0).getValue();
            File dir = new File("plugins/" + description.getName());
            if(!dir.exists()) {
                dir.mkdirs();
            }
            File depends = new File(dir, "lib");
            if(!depends.exists()) {
                depends.mkdirs();
            }
            for (File listFile : depends.listFiles()) {
                loader.addURL(listFile.toURI().toURL());
            }
            Class<?> pluginMain = Class.forName(mainName, true, loader);
            if(pluginMain.getSuperclass() == null || !pluginMain.getSuperclass().equals(JavaPlugin.class)) {
                throw new InvalidPluginException("Main class must extends PluginBase!");
            }
            Class<? extends JavaPlugin> plugin = pluginMain.asSubclass(JavaPlugin.class);
            Constructor<? extends JavaPlugin> constructor = plugin.getConstructor();
            JavaPlugin javaPlugin = constructor.newInstance();
            javaPlugin.init(this, description, dir, file ,loader);
            javaPlugin.setEnabled(true);
            plugins.put(description.getName(), javaPlugin);
            pluginsLoader.put(description.getName(), loader);
            p.set(javaPlugin);
            if(listenerClasses.size() > 0) {
                listenerClasses.forEach(it -> {
                    try {
                        Class<?> listener = Class.forName(it, true, loader);
                        EventUtils.registerEvents(p.get(), listener);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
            commandClasses.forEach((k, v) -> {
                try {
                    Class<?> command = Class.forName(k, true, loader);
                    Validate.isTrue(CommandExecutor.class.isAssignableFrom(command), "CommandVisitor must implements CommandExecutor");
                    Class<? extends CommandExecutor> commandExecutor = command.asSubclass(CommandExecutor.class);
                    Constructor<? extends CommandExecutor> commandConstructor = commandExecutor.getConstructor();
                    CommandExecutor exec = commandConstructor.newInstance();
                    PluginCommand cmd = new PluginCommand(v.getName(), p.get());
                    cmd.setExecutor(exec);
                    LRobot.getCommandMap().register(cmd);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        }
        return p.get();
    }

    @Override
    public void enablePlugin(final Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        PluginEnableEvent event = new PluginEnableEvent(plugin);
        EventUtils.callEvent(event);
        logger.info("Enabling " + plugin.getDescription().getName() + " Version: " + plugin.getDescription().getVersion() + " Author: " + plugin.getDescription().getAuthor());
        plugin.onEnable();
    }

    @Override
    public void disablePlugin(final Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        PluginDisableEvent event = new PluginDisableEvent(plugin);
        EventUtils.callEvent(event);
        logger.info("Stopping " + plugin.getDescription().getName() + " Version: " + plugin.getDescription().getVersion() + " Author: " + plugin.getDescription().getAuthor());
        plugin.onDisable();
        plugin.setEnabled(false);
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Object listener, final Plugin plugin, final boolean isStatic) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");

        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            methods = new HashSet<>(publicMethods.length, Float.MAX_VALUE);
            Collections.addAll(methods, publicMethods);
            Collections.addAll(methods, listener.getClass().getDeclaredMethods());
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().error("Plugin " + plugin.getDescription().getName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            if(isStatic) {
                if(!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
            }
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger().error(plugin.getDescription().getName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.computeIfAbsent(eventClass, k -> new HashSet<>());

            EventExecutor executor = (object, event) -> {
                try {
                    if (!eventClass.isAssignableFrom(event.getClass())) {
                        return;
                    }
                    method.invoke(object, event);
                } catch (InvocationTargetException ex) {
                    throw new EventException(ex.getCause());
                } catch (Throwable t) {
                    throw new EventException(t);
                }
            };
            eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
        }
        return ret;
    }

    private List<InputStream> getAllClassesInputStreamByFile(File file) throws IOException {
        List<InputStream> list = new ArrayList<>();
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()) {
            JarEntry entry = jarEntries.nextElement();
            String name = entry.getName();
            if(entry.isDirectory() || !name.endsWith(".class")) {
                continue;
            }
            list.add(jarFile.getInputStream(entry));
        }
        return list;
    }

    private Map<String, Object> asmAnnotationValuesToMap(List<Object> annotations) {
        Validate.notNull(annotations, "Annotation values can not be null");
        Map<String, Object> annotationMap = new HashMap<>();
        for(int i = 1; i < annotations.size(); i += 2) {
            if(annotations.get(i) != null) {
                annotationMap.put((String) annotations.get(i - 1), annotations.get(i));
            }
        }
        return annotationMap;
    }

    private String asmAnnotationDescToClassPath(String name) {
        Validate.notNull(name, "Annotation desc can not be null");
        String anno = name.replaceAll("/", ".");
        return anno.substring(1, anno.length() - 1);
    }

    private List<AnnotationNode> getASMClassAnnotations(ClassNode node) {
        Validate.notNull(node, "ClassNode can not be null");
        return node.invisibleAnnotations;
    }

    private String getASMClassName(String name) {
        Validate.notNull(name, "Class name can not be null");
        return name.replace("/", ".");
    }

    private ClassNode readClass(InputStream is) throws IOException {
        Validate.notNull(is, "Class InputStream can not be null");
        ClassReader reader = new ClassReader(is);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        return node;
    }

    public Plugin getPluginByName(String name) {
        Validate.notNull(name, "Plugin name can not be null");
        return plugins.get(name);
    }

    public PluginClassLoader getPluginLoaderByName(String name) {
        Validate.notNull(name, "Plugin name can not be null");
        return pluginsLoader.get(name);
    }

    public Plugin getPluginMainByName(Class<? extends Plugin> plugin) {
        Validate.notNull(plugin, "Plugin class can not be null");
        return pluginsMain.get(plugin);
    }

    public Map<String, PluginClassLoader> getPluginsLoader() {
        return pluginsLoader;
    }

    public Map<String, Plugin> getPlugins() {
        return plugins;
    }

    public Map<Class<? extends Plugin>, Plugin> getPluginsMain() {
        return pluginsMain;
    }
}
