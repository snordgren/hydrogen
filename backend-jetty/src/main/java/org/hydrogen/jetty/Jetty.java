package org.hydrogen.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hydrogen.Handler;
import org.hydrogen.Servlet;

/**
 * Servlet runtime for using Jetty.
 */
public final class Jetty {
    private static final int DEFAULT_PORT = 8080;

    private Jetty() {

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
        Servlet servlet = new Servlet(handler);
        Server server = new Server(port);
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
        JettyServer server = start(port, handler);
        try {
            serverThrowingConsumer.acceptThrows(server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        server.stop();
    }
}
