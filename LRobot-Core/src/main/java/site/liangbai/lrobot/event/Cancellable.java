package site.liangbai.lrobot.event;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
