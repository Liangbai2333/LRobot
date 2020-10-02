package site.liangbai.lrobot.contact.message;

public enum ServiceMessageType {
    XML(60),
    JSON(1),
    LongMessage(35),
    ForwardMessage(35);

    private final int serviceId;

    private ServiceMessageType(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceId() {
        return serviceId;
    }
}
