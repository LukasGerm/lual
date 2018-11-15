package github.lual.net;

@FunctionalInterface
public interface MessageReceiver {

    void onMessageReceived(String message);
}
