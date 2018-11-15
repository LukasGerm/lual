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

/**
 * A TlsClient based on Netty
 */
public class TlsClient implements Closeable {

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private final MessageChannelHandlerAdapter messageChannelHandlerAdapter = new MessageChannelHandlerAdapter();
    private final Bootstrap bootstrap;

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
    }

    /**
     * Connects to the server.
     *
     * @throws InterruptedException
     */
    public void connect() throws InterruptedException {
        // You can't double-connect
        if (this.channelFuture != null) {
            return;
        }

        this.channelFuture = this.bootstrap.connect(host, port).sync();
        // channelFuture.channel().closeFuture().sync();
    }

    @Override
    public void close() throws IOException {
        this.eventLoopGroup.shutdownGracefully();
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
     * Returns the handler adapter. There you can attach or detach the message receivers.
     *
     * @return handler adapter
     */
    public MessageChannelHandlerAdapter getHandlerAdapter() {
        return this.messageChannelHandlerAdapter;
    }
}
