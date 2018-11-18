package github.lual.net;

public class TlsTest {

    public static void main(String[] args) throws Exception {
        String filePath = Thread.currentThread().getContextClassLoader().getResource("server.pfx").getFile();
        System.setProperty("javax.net.ssl.keyStore", filePath);
        System.setProperty("javax.net.ssl.keyStorePassword", "changeme");
        System.setProperty("javax.net.ssl.trustStore", filePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "changeme");
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
