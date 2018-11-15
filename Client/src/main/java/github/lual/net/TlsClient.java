package github.lual.net;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class TlsClient implements Closeable {

    private static final int BUFFER_SIZE = 4096;

    private AsynchronousSocketChannel client;
    private InetSocketAddress hostAddress;
    private final Queue<MessageReceiver> receiverQueue;
    private AtomicBoolean listening;

    public TlsClient(String host, int port) throws IOException {
        this.receiverQueue = new ConcurrentLinkedQueue<>();
        this.listening = new AtomicBoolean();
        this.client = AsynchronousSocketChannel.open();
        this.hostAddress = new InetSocketAddress(host, port);
    }

    public synchronized void connect() throws ExecutionException, InterruptedException {
        this.client.connect(this.hostAddress).get();
        System.out.println("Connected");
    }

    @Override
    public synchronized void close() throws IOException {
        this.listening.set(false);
        this.client.close();
        System.out.println("Closed");
    }

    public void attachReceiver(MessageReceiver receiver) {
        if (receiverQueue.contains(receiver)) {
            return;
        }
        receiverQueue.add(receiver);
        System.out.println("Attached receiver");
    }

    public void detachReceiver(MessageReceiver receiver) {
        if (!receiverQueue.contains(receiver)) {
            return;
        }
        receiverQueue.remove(receiver);
        System.out.println("Removed receiver");
    }

    public synchronized Future<Integer> sendMessage(String message) {
        if (this.client.isOpen()) {
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            System.out.println("Sending message");
            return this.client.write(ByteBuffer.wrap(bytes));
        }
        return null;
    }

    public Runnable getListenLoopRunnable() {
        return () -> {
            try {
                listenLoop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void listenLoop() throws ExecutionException, InterruptedException, IOException {
        this.listening.set(true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        boolean reading = false;

        while (listening.get()) {
            if (!client.isOpen()) {
                break;
            }
            buffer.clear();
            Future<Integer> readFuture = this.client.read(buffer);
            int readResult = readFuture.get();

            // Stream/connection broken, no further operations on this connection
            if (readResult == -1) {
                bos.close();
                reading = false;
                bos = new ByteArrayOutputStream();
                continue;
            }

            System.out.println("Read result " + readResult);

            // If data exists, write to output stream
            if (readResult > 0) {
                bos.write(buffer.array(), 0, readResult);
            }

            // When continuing reading, but buffer size is not full, call receivers
            if (reading && readResult < BUFFER_SIZE) {
                byte[] bytes = bos.toByteArray();
                String message = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Read message " + message);
                receiverQueue.forEach(receiver -> receiver.onMessageReceived(message));
                reading = false;
                bos.close();
                bos = new ByteArrayOutputStream();
            }

            // If there are more bytes, repeat reading
            if (readResult == BUFFER_SIZE) {
                reading = true;
                continue;
            }
        }
    }

}
