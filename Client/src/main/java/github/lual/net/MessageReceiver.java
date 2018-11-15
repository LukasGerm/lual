package github.lual.net;

/**
 * Callback handler for receiving messages.
 */
@FunctionalInterface
public interface MessageReceiver {

    /**
     * Gets called when a message was received from the server.
     *
     * @param message   The message received or null, if an error occured.
     * @param throwable If not null, the error that occured.
     */
    void onMessageReceived(String message, Throwable throwable);
}
