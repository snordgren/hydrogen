package jetty;

import org.eclipse.jetty.server.Server;
import org.hydrogen.util.ExceptionUtils;

public class JettyServer implements org.hydrogen.Server {
    private final Server server;

    JettyServer(Server server) {
        this.server = server;
    }

    @Override
    public void join() {
        ExceptionUtils.run(server::join);
    }

    @Override
    public void stop() {
        ExceptionUtils.run(server::stop);
    }
}
