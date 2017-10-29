package org.hydrogen.jetty;

import org.hydrogen.TextResponse;
import org.junit.Test;

public class JettyServerTest {
    @Test
    public void testRunServer() {
        JettyServer.start(request -> TextResponse.ok("Hello, world!"));
    }
}
