package org.hydrogen.jetty;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.hydrogen.HTMLResponse;
import org.hydrogen.Server;
import org.hydrogen.TextResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JettyServerTest {
    private final int port = 8080;
    private final String localhost = "http://localhost:" + port;

    @Test
    public void testHTML() throws UnirestException {
        final String expected = "<h1>Hi!</h1>";
        Server server = JettyServer.start(request -> HTMLResponse.ok(expected), port);
        String result = Unirest.get(localhost).asString().getBody();
        assertEquals(expected, result);
        server.stop();
    }

    @Test
    public void testString() throws UnirestException {
        final String expected = "Hello, world!";
        Server server = JettyServer.start(request -> TextResponse.ok(expected), port);
        String result = Unirest.get(localhost).asString().getBody();
        assertEquals(expected, result);
        server.stop();
    }
}
