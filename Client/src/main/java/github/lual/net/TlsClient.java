package github.lual.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A TlsClient based on Netty
 */
public class TlsClient implements Closeable {

    private final ExecutorService executorService = Executors.newFixedThreadPool(Math.min(2, Runtime.getRuntime().availableProcessors()));
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private final MessageChannelHandlerAdapter messageChannelHandlerAdapter = new MessageChannelHandlerAdapter(executorService);
    private final Bootstrap bootstrap;
    private final AtomicBoolean shutdownHookCalled = new AtomicBoolean();

    private final String host;
    private final int port;

    private ChannelFuture channelFuture;

    public TlsClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        // Setup the netty environment
        bootstrap = new Bootstrap() //
                .group(eventLoopGroup) //
                .channel(NioSocketChannel.class) //
                .option(ChannelOption.SO_KEEPALIVE, true) //
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettySslHandler(), messageChannelHandlerAdapter);
                    }
                });

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Connects to the server.
     *
     * @throws InterruptedException
     */
    public void connect() throws InterruptedException {
        if (shutdownHookCalled.get()) {
            throw new IllegalStateException("Client already shutted down. You have to recreate a new client.");
        }
        // You can't double-connect
        if (this.channelFuture != null) {
            return;
        }

        this.channelFuture = this.bootstrap.connect(host, port).sync();
        // channelFuture.channel().closeFuture().sync();
    }

    @Override
    public synchronized void close() throws IOException {
        if (shutdownHookCalled.get()) {
            return;
        }
        shutdownHookCalled.set(true);
        this.eventLoopGroup.shutdownGracefully();
        this.executorService.shutdown();
    }

    /**
     * Sends the message to the server and returns the underlying ChannelFuture. Call sync on the ChannelFuture to make it blocking.
     *
     * @param message The message to send
     * @return The underlying ChannelFuture
     * @throws InterruptedException
     */
    public ChannelFuture sendMessage(String message) throws InterruptedException {
        ByteBuf buffer = Unpooled.copiedBuffer(message.getBytes(StandardCharsets.UTF_8));
        return this.channelFuture.channel().writeAndFlush(buffer);
    }

    /**
     * Adds a message receiver to the receiver adapter.
     *
     * @param messageReceiver The receiver to add.
     */
    public void addReceiver(MessageReceiver messageReceiver) {
        this.messageChannelHandlerAdapter.attachReceiver(messageReceiver);
    }

    /**
     * Removes a message receiver from the receiver adapter.
     *
     * @param messageReceiver The receiver to remove.
     */
    public void removeReceiver(MessageReceiver messageReceiver) {
        this.messageChannelHandlerAdapter.detachReceiver(messageReceiver);
    }
}
