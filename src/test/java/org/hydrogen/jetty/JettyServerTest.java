package org.hydrogen.jetty;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.hydrogen.HTMLResponse;
import org.hydrogen.RequestMethod;
import org.hydrogen.Server;
import org.hydrogen.TextResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JettyServerTest {
    private static final Counter portCounter = new Counter(8000);

    @Test
    public void testHTML() throws UnirestException {
        int port = portCounter.next();
        String localhost = "http://localhost:" + port;
        final String expected = "<h1>Hi!</h1>";
        Server server = JettyServer.start(request -> {
            assertEquals(RequestMethod.GET, request.getMethod());
            return HTMLResponse.ok(expected);
        }, port);
        String result = Unirest.get(localhost).asString().getBody();
        assertEquals(expected, result);
        server.stop();
    }

    @Test
    public void testString() throws UnirestException {
        int port = portCounter.next();
        String localhost = "http://localhost:" + port;
        final String expected = "Hello, world!";
        Server server = JettyServer.start(request -> {
            assertEquals(RequestMethod.GET, request.getMethod());
            return TextResponse.ok(expected);
        }, port);
        String result = Unirest.get(localhost).asString().getBody();
        assertEquals(expected, result);
        server.stop();
    }

    @Test
    public void testPost() throws UnirestException {
        int port = portCounter.next();
        String localhost = "http://localhost:" + port;
        final String expected = "Ok.";
        Server server = JettyServer.start(request -> {
            assertEquals(RequestMethod.POST, request.getMethod());
            return TextResponse.ok(expected);
        }, port);
        assertEquals(expected, Unirest.post(localhost).asString().getBody());
        server.stop();
    }

    private static class Counter {
        private int currentValue;

        public Counter(int initialValue) {
            currentValue = initialValue;
        }

        public int next() {
            int current = currentValue;
            currentValue++;
            return current;
        }
    }
}
