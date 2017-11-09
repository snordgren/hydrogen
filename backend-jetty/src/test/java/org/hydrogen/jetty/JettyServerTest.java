package org.hydrogen.jetty;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import org.hydrogen.Handler;
import org.hydrogen.RequestMethod;
import org.hydrogen.Response;
import org.hydrogen.Router;
import org.hydrogen.Session;
import org.json.JSONArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JettyServerTest {
    private static final Counter portCounter = new Counter(8000);

    @Test
    public void testHTML() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected = "<h1>Hi!</h1>";
        Jetty.use(port, request -> {
            assertEquals(RequestMethod.GET, request.getMethod());
            return Response.ok().html(expected);
        }, server -> {
            String result = Unirest.get(localhost).asString().getBody();
            assertEquals(expected, result);
        });
    }

    @Test
    public void testJSON() {
        int port = portCounter.next();
        String localhost = localhost(port);
        Handler handler = request -> Response.ok().json("{ values: [1, 2, 3] }");
        Jetty.use(port, handler, server -> {
            JSONArray values = Unirest.get(localhost).asJson().getBody().getObject()
                    .getJSONArray("values");
            assertEquals(1, values.getInt(0));
            assertEquals(2, values.getInt(1));
            assertEquals(3, values.getInt(2));
        });
    }

    @Test
    public void testMultipleRoutes() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected0 = "Ok: 0";
        final String expected1 = "Ok: 1";
        final String expectedNotFound = "404 Not Found";
        Router router = Router.builder()
                .notFound(req -> Response.notFound().text(expectedNotFound))
                .get("/0", request -> Response.ok().text(expected0))
                .get("/1", request -> Response.ok().text(expected1))
                .build();
        Jetty.use(port, router, server -> {
            assertEquals(expected0, Unirest.get(localhost + "/0").asString().getBody());
            assertEquals(expected1, Unirest.get(localhost + "/1").asString().getBody());
            assertEquals(expectedNotFound,
                    Unirest.get(localhost + "/2").asString().getBody());
        });
    }

    @Test
    public void testPost() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected = "Ok.";
        Jetty.use(port, request -> {
            assertEquals(RequestMethod.POST, request.getMethod());
            return Response.ok().text(expected);
        }, server -> {
            assertEquals(expected, Unirest.post(localhost).asString().getBody());
        });
    }

    @Test
    public void testText() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected = "Hello, world!";
        Jetty.use(port, request -> {
            assertEquals(RequestMethod.GET, request.getMethod());
            return Response.ok()
                    .text(expected);
        }, server -> {
            GetRequest request = Unirest.get(localhost);
            String result = request.asString().getBody();
            assertEquals(expected, result);
        });
    }

    @Test
    public void testXML() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected = "<awful>truly</awful>";
        Jetty.use(port, request -> Response.ok().xml(expected), server -> {
            assertEquals(expected, Unirest.get(localhost).asString().getBody());
        });
    }

    @Test
    public void testHeader() {
        int port = portCounter.next();
        String localhost = localhost(port);
        String header = "Cache-Control";
        String expected = "max-age=3600";

        Jetty.use(port, request -> Response.ok()
                .header(header, expected)
                .text("{}"), server -> {
            GetRequest request = Unirest.get(localhost);
            HttpResponse<JsonNode> response = request.asJson();
            Headers headers = response.getHeaders();
            assertEquals(expected, headers.getFirst(header));
        });
    }

    @Test
    public void testSession() {
        int port = portCounter.next();
        String localhost = localhost(port);
        Jetty.use(port, request -> {
            Session session = request.getSession();
            int timesVisited;
            if (session.hasAttribute("TimesVisited")) {
                timesVisited = (Integer) session.getAttribute("TimesVisited") + 1;
            } else timesVisited = 1;
            Session newSession = session.withAttribute("TimesVisited", timesVisited);
            return Response.ok()
                    .session(newSession)
                    .text(newSession.getAttribute("TimesVisited").toString());
        }, server -> {
            assertEquals("1", Unirest.post(localhost).asString().getBody());
            assertEquals("2", Unirest.post(localhost).asString().getBody());
            assertEquals("3", Unirest.post(localhost).asString().getBody());
        });
    }

    @Test
    public void testQueryParams() {
        int port = portCounter.next();
        String localhost = localhost(port) + "?name=Me&password=1234&pin=9999";
        Jetty.use(port, request -> {
            assertEquals("/", request.getUrl());
            assertFalse(request.getQueryParams().isEmpty());
            assertEquals("Me", request.getQueryParam("name"));
            assertEquals("1234", request.getQueryParam("password"));
            assertEquals("9999", request.getQueryParam("pin"));
            return Response.ok().text("Success!");
        }, server -> Unirest.post(localhost).asString());
    }

    private static String localhost(int port) {
        return "http://localhost:" + port;
    }

    private static class Counter {
        private int currentValue;

        Counter(int initialValue) {
            currentValue = initialValue;
        }

        int next() {
            int current = currentValue;
            currentValue++;
            return current;
        }
    }
}
