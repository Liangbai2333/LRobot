package site.liangbai.lrobot.plugin.exception;

public class InvalidPluginException extends RuntimeException {
    public InvalidPluginException(String s) {
        super(s);
    }

    public InvalidPluginException(Throwable t) { super(t); }

    public InvalidPluginException(String s, Throwable t) { super(s, t); }
}
