package site.liangbai.lrobot.plugin;

import net.mamoe.mirai.utils.PlatformLogger;
import net.mamoe.mirai.utils.SimpleLogger;
import org.fusesource.jansi.Ansi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LRobotLogger extends PlatformLogger {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public LRobotLogger(@Nullable String identity) {
        super(identity);
    }

    @Override
    protected void printLog(@Nullable String message, @NotNull SimpleLogger.LogPriority priority){
        Date date = new Date();
        String invoke = Ansi.ansi().reset() + "[" + sdf.format(date) + " " + priority.name() + "] " + message + Ansi.ansi().reset();
        getOutput().invoke(invoke);
    }

    protected void printLog(@NotNull String prefix, @Nullable String message, @NotNull SimpleLogger.LogPriority priority){
        printLog("[" + prefix + "] " + message, priority);
    }
}
