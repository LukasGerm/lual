package github.lual.net;

import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * A SslHandler for Netty, that uses the a TLS SSLContext in client-mode.
 */
public class NettySslHandler extends SslHandler {

    public NettySslHandler() {
        super(sslEngine());
    }

    private static SSLEngine sslEngine() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(true);
            return sslEngine;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // FIXME: We want to validate a static certificate instead, but for testing purpose we trust all certs
    private static TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};
}
