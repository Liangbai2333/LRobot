package site.liangbai.lrobot.util;

import jline.console.ConsoleReader;
import site.liangbai.lrobot.LRobot;

import java.io.IOException;

/**
 * Jline处理指令的线程.
 */
public final class JlineReaderThread implements Runnable {
    private final ConsoleReader reader;

    public JlineReaderThread() throws IOException {
        this.reader = new ConsoleReader(System.in, System.out);
        this.reader.setExpandEvents(false);
    }

    @Override
    public void run() {
        while(true) {
            try {
                reader.getTerminal().restore();
                String line = reader.readLine();
                LRobot.getCommandMap().dispatch(LRobot.INSTANCE, 0, line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ConsoleReader getReader() {
        return reader;
    }
}
