package site.liangbai.lrobot.contact.message.xml;

public class ItemBuilder {
    private int bg = 0;
    private int layout = 4;
    private final StringBuilder sb = new StringBuilder();

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public StringBuilder getBuilder() {
        return sb;
    }

    public void summary(String text, String color) {
        this.sb.append(String.format("<summary color='%s'>%s</summary>", color, text));
    }

    public void summary(String text) {
        this.sb.append(String.format("<summary color='%s'>%s</summary>", "#000000", text));
    }

    public void picture(String coverUrl) {
        this.sb.append(String.format("<picture cover='%s'/>", coverUrl));
    }

    public void title(String text, int size, String color) {
        this.sb.append(String.format("<title size='%d' color='%s'>%s</title>", size, color, text));
    }

    public void title(String text, String color) {
        this.sb.append(String.format("<title size='%d' color='%s'>%s</title>", 25, color, text));
    }

    public void title(String text, int size) {
        this.sb.append(String.format("<title size='%d' color='%s'>%s</title>", size, "#000000", text));
    }

    public void title(String text) {
        this.sb.append(String.format("<title size='%d' color='%s'>%s</title>", 25, "#000000", text));
    }

    public String getText() {
        return String.format("<item bg='%d' layout='%d'>%s</item>", getBg(), getLayout(), getBuilder().toString());
    }

    @Override
    public String toString() {
        return getText();
    }
}
