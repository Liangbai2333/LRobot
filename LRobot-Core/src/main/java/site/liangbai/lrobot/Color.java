package site.liangbai.lrobot;

import org.fusesource.jansi.Ansi;

public final class Color {
    public static final String BLACK = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString();
    public static final String DARK_BLUE = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString();
    public static final String DARK_GREEN = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString();
    public static final String DARK_AQUA = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString();
    public static final String DARK_RED = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString();
    public static final String DARK_PURPLE = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString();
    public static final String GOLD = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString();
    public static final String GRAY = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString();
    public static final String DARK_GRAY = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString();
    public static final String BLUE = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString();
    public static final String GREEN = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString();
    public static final String AQUA = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString();
    public static final String RED = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString();
    public static final String LIGHT_PURPLE = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString();
    public static final String YELLOW = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString();
    public static final String WHITE = Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString();
    public static final String MAGIC = Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString();
    public static final String BOLD = Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString();
    public static final String STRIKETHROUGH = Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString();
    public static final String UNDERLINE = Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString();
    public static final String ITALIC = Ansi.ansi().a(Ansi.Attribute.ITALIC).toString();
    public static final String RESET = Ansi.ansi().a(Ansi.Attribute.RESET).toString();
}
