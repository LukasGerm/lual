package github.lual.net;

public class TlsTest {

    public static void main(String[] args) throws Exception {
        // in the handler "MessageReceiver" we get all messages and errors
        MessageReceiver messageReceiver = (message, throwable) -> {
            if (message != null) {
                System.out.println(message);
            } else {
                System.err.println(throwable.getMessage());
            }
        };

        // create the tlsclient, attach the handler and connect to the server
        TlsClient client = new TlsClient("localhost", 8000);
        client.getHandlerAdapter().attachReceiver(messageReceiver);
        client.connect();

        // we send a basic message to the server
        client.sendMessage("0|test|test2");
    }
}
