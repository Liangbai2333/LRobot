package site.liangbai.lrobot.contact.message.xml;

public class XmlMessageBuilder {
    private int templateId = 1;
    private int serviceId = 1;
    private String action = "plugin";
    private String actionData = "";
    private String brief = "";
    private int flag = 3;
    private String url = ""; // TODO: 2019/12/3 unknown
    private String sourceName = "";
    private String sourceIconURL = "";
    private ItemBuilder builder = new ItemBuilder();

    public int getTemplateId() {
        return templateId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getFlag() {
        return flag;
    }

    public String getActionData() {
        return actionData;
    }

    public String getBrief() {
        return brief;
    }

    public String getSourceIconURL() {
        return sourceIconURL;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public String getUrl() {
        return url;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setSourceIconURL(String sourceIconURL) {
        this.sourceIconURL = sourceIconURL;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return String.format("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>" +
                "<msg templateID='%d' serviceID='%d' action='%s' actionData='%s' brief='%s' flag='%d' url='%s'>" +
                builder.toString() +
                "<source name='%s' icon='%s'/>" +
                "</msg>", getTemplateId(), getServiceId(), getAction(), getActionData(), getBrief(), getFlag(), getUrl(), getSourceName(), getSourceIconURL());
    }

    @Override
    public String toString() {
        return getText();
    }

    public ItemBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(ItemBuilder builder) {
        this.builder = builder;
    }

    public void source(String name, String iconURL) {
        setSourceName(name);
        setSourceIconURL(iconURL);
    }
}
