package org.hydrogen.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.hydrogen.Handler;

import java.util.concurrent.TimeUnit;

public class JettyServer implements org.hydrogen.Server {
    private static final int DEFAULT_PORT = 8080;

    private final Handler handler;
    private final int port;
    private final ServerConnector serverConnector;
    private final Server server;

    public JettyServer(Handler handler, int port) {
        this.handler = handler;
        this.port = port;
        Server initialServer = new Server(new QueuedThreadPool(100, 4, 1000));
        serverConnector = createSocketConnector(initialServer, "0.0.0.0", port);
        serverConnector.setPort(port);
        server = serverConnector.getServer();
        server.setConnectors(new Connector[]{serverConnector});
        server.setHandler(new HandlerWrapper(handler));
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ServerConnector createSocketConnector(
            Server server,
            String host,
            int port) {
        HttpConfiguration config = new HttpConfiguration();
        config.setSecureScheme("https");
        config.addCustomizer(new ForwardedRequestCustomizer());
        HttpConnectionFactory factory = new HttpConnectionFactory(config);
        ServerConnector connector = new ServerConnector(server, factory);
        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(-1);
        connector.setHost(host);
        connector.setPort(port);
        return connector;
    }

    public static JettyServer start(Handler handler) {
        return start(handler, DEFAULT_PORT);
    }

    public static JettyServer start(Handler handler, int port) {
        return new JettyServer(handler, port);
    }


    @Override
    public void join() {
        try {
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
