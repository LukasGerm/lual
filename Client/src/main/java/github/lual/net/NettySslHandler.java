package github.lual.net;

import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * A SslHandler for Netty, that uses the a TLS SSLContext in client-mode.
 */
public class NettySslHandler extends SslHandler {

    public NettySslHandler() {
        super(sslEngine());
    }

    private static SSLEngine sslEngine() {
        try {
            final SSLContext sslContext = SSLContext.getDefault();
            final SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(true);
            return sslEngine;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
