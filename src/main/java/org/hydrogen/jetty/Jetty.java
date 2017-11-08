package org.hydrogen.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hydrogen.Handler;
import org.hydrogen.Servlet;
import org.hydrogen.util.ExceptionUtils;
import org.hydrogen.util.ThrowingConsumer;

public class Jetty {
    public static JettyServer start(int port, Handler handler) {
        Servlet servlet = new Servlet(handler);
        Server server = new Server(port);
        ServletContextHandler servletContextHandler = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(servlet), "/*");
        server.insertHandler(servletContextHandler);
        ExceptionUtils.run(server::start);
        return new JettyServer(server);
    }

    public static void use(int port, Handler handler,
            ThrowingConsumer<org.hydrogen.Server> serverThrowingConsumer) {
        JettyServer server = start(port, handler);
        ExceptionUtils.run(() -> serverThrowingConsumer.acceptThrows(server));
        server.stop();
    }
}
