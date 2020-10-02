package site.liangbai.lrobot.plugin;

import java.util.Map;

public class PluginDescription {
    private final String name;
    private final String version;
    private final String main;
    private final String author;

    public PluginDescription(Map<String, Object> map, String main) {
        this.name = (String) map.get("name");
        this.version = (String) map.get("version");
        this.main = main;
        this.author = (String) map.get("author");
    }

    public PluginDescription(String name, String version, String main, String author, String depends, String softDepends){
        this.name = name;
        this.version = version;
        this.main = main;
        this.author = author;
    }

    private String[] toArray(String array) {
        if(array == null) {
            return null;
        }
        String temp = array.substring(1, array.lastIndexOf("]") - 1);
        String[] tempArray = temp.split(",");
        String[] returnArray = new String[tempArray.length + 1];
        for (int i = 0; i < tempArray.length; i++) {
            returnArray[i] = tempArray[i].trim();
        }
        return returnArray;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getMain() {
        return main;
    }

    public String getVersion() {
        return version;
    }
}
