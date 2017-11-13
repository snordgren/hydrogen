package org.hydrogen.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.hydrogen.Handler;
import org.hydrogen.Servlet;
import org.hydrogen.security.SSLCertificate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Servlet runtime for using Jetty.
 */
public final class Jetty {
    private static final int DEFAULT_PORT = 8080;

    private Jetty() {

    }

    private static HttpConnectionFactory createConnectionFactory() {
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme("https");
        httpConfig.addCustomizer(new ForwardedRequestCustomizer());
        return new HttpConnectionFactory(httpConfig);
    }

    private static Connector createConnector(int port,
            Server server,
            Optional<SSLCertificate> cert) {
        HttpConnectionFactory factory = createConnectionFactory();
        return initializeConnector(port, server, factory,
                cert.map(Jetty::createSSLContextFactory));
    }

    private static SslContextFactory createSSLContextFactory(SSLCertificate cert) {
        SslContextFactory factory = new SslContextFactory(cert.getKeystoreFile());
        factory.setKeyStorePassword(cert.getKeystorePassword());
        cert.getTruststoreFile().ifPresent(factory::setTrustStorePath);
        cert.getTruststorePassword().ifPresent(factory::setTrustStorePassword);
        return factory;
    }

    private static Connector initializeConnector(int port,
            Server server,
            HttpConnectionFactory httpConnectionFactory,
            Optional<SslContextFactory> sslContextFactory) {

        ServerConnector serverConnector = sslContextFactory.map(
                factory -> new ServerConnector(server, factory, httpConnectionFactory))
                .orElseGet(() -> new ServerConnector(server, httpConnectionFactory));
        serverConnector.setHost("");
        serverConnector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        serverConnector.setPort(port);
        serverConnector.setSoLingerTime(-1);
        return serverConnector;
    }

    /**
     * Starts a new server on the default port (8080) using a handler. Use
     * http://localhost:8080 to access the server.
     *
     * @param handler Request handler.
     * @return Server control interface object.
     */
    public static JettyServer start(Handler handler) {
        return start(DEFAULT_PORT, handler);
    }

    /**
     * Starts a new server on the specified port using a handler.
     *
     * @param port The port to listen to requests on.
     * @param handler Request handler.
     * @return Server control interface object.
     */
    public static JettyServer start(int port, Handler handler) {
        return startJetty(port, handler, Optional.empty());
    }

    public static JettyServer startSecure(int port,
            Handler handler,
            SSLCertificate cert) {
        return startJetty(port, handler, Optional.of(cert));
    }

    private static JettyServer startJetty(int port,
            Handler handler,
            Optional<SSLCertificate> cert) {
        Servlet servlet = new Servlet(handler);
        Server server = new Server(port);
        Connector connector = createConnector(port, server, cert);
        server.setConnectors(new Connector[]{connector});
        ServletContextHandler servletContextHandler = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(servlet), "/*");
        server.insertHandler(servletContextHandler);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new JettyServer(server);
    }

    /**
     * Utility method for starting a server using a handler, running a single
     * function on it, and then stopping it. Useful for testing with a real
     * server.
     *
     * @param port The port to launch the server on.
     * @param handler The request handler.
     * @param serverThrowingConsumer The code to run on this server, before
     * stopping it.
     */
    public static void use(int port, Handler handler,
            ThrowingConsumer<org.hydrogen.Server> serverThrowingConsumer) {
        useInternal(serverThrowingConsumer,
                () -> start(port, handler));
    }

    public static void useSecure(int port,
            Handler handler,
            SSLCertificate sslCertificate,
            ThrowingConsumer<org.hydrogen.Server> serverThrowingConsumer) {
        useInternal(serverThrowingConsumer,
                () -> startSecure(port, handler, sslCertificate));
    }

    private static void useInternal(ThrowingConsumer<org.hydrogen.Server> serverThrowingConsumer,
            Supplier<JettyServer> serverSupplier) {
        JettyServer server = serverSupplier.get();
        try {
            serverThrowingConsumer.acceptThrows(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        server.stop();
    }
}
