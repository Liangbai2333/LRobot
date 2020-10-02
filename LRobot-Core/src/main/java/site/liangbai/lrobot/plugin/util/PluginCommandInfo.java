package site.liangbai.lrobot.plugin.util;

public final class PluginCommandInfo {
    private final String name;
    private final String description;
    private final String usage;

    public PluginCommandInfo(String name, String description, String usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }
}
