package github.lual.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TLsTest {

    public static void main(String[] args) throws Exception {
        TlsClient client = new TlsClient("127.0.0.1", 8000);
        client.attachReceiver(message -> System.out.println(message));
        client.connect();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(client.getListenLoopRunnable());

        client.sendMessage("0|test|test2").get();
        System.out.println("Message sent");
    }
}
