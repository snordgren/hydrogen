package org.hydrogen.jetty;

import org.hydrogen.HTMLResponse;
import org.hydrogen.Server;
import org.junit.Test;

public class JettyServerTest {
    @Test
    public void testHTML() {
        Server server = JettyServer.start(request -> HTMLResponse.ok("<h1>Hi!</h1>"));
        server.join();
    }
}
