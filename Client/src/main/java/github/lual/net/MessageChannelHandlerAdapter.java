package github.lual.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * ChannelInboundHandlerAdapter that receives the messages as a String and pushes them to the attached MessageReceivers.
 */
public class MessageChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    private final Queue<MessageReceiver> receivers = new ConcurrentLinkedQueue<>();
    private final ExecutorService executorService;

    public MessageChannelHandlerAdapter(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Attaches the receiver to the receiver queue.
     *
     * @param messageReceiver The receiver to attach.
     */
    public void attachReceiver(MessageReceiver messageReceiver) {
        if (receivers.contains(messageReceiver)) {
            return;
        }
        receivers.add(messageReceiver);
    }

    /**
     * Detaches the receiver from the receiver queue.
     *
     * @param messageReceiver The receiver to detach.
     */
    public void detachReceiver(MessageReceiver messageReceiver) {
        if (!receivers.contains(messageReceiver)) {
            return;
        }
        receivers.remove(messageReceiver);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        try {
            String message = buffer.toString(StandardCharsets.UTF_8);
            receivers.forEach(receiver -> notifyReceiver(receiver, message, null));
        } finally {
            buffer.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        receivers.forEach(receiver -> notifyReceiver(receiver, null, cause));
    }

    private void notifyReceiver(MessageReceiver messageReceiver, String message, Throwable throwable) {
        executorService.submit(() -> messageReceiver.onMessageReceived(message, throwable));
    }
}
