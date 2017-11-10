package org.hydrogen.jetty;

import org.eclipse.jetty.server.Server;

public final class JettyServer implements org.hydrogen.Server {
    private final Server server;

    JettyServer(Server server) {
        this.server = server;
    }

    @Override
    public void join() {
        try {
            server.join();
        } catch (InterruptedException e) {
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
