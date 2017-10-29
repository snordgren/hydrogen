package org.hydrogen;

public interface Server {

    /**
     * Await server stop.
     */
    void join();

    /**
     * Stops this server.
     */
    void stop();
}
