package jetty;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import org.hydrogen.HTMLResponse;
import org.hydrogen.Handler;
import org.hydrogen.JSONResponse;
import org.hydrogen.RequestMethod;
import org.hydrogen.Router;
import org.hydrogen.TextResponse;
import org.hydrogen.XMLResponse;
import org.json.JSONArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JettyServerTest {
    private static final Counter portCounter = new Counter(8000);

    @Test
    public void testHTML() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected = "<h1>Hi!</h1>";
        Jetty.use(port, request -> {
            assertEquals(RequestMethod.GET, request.getMethod());
            return HTMLResponse.ok(expected);
        }, server -> {
            String result = Unirest.get(localhost).asString().getBody();
            assertEquals(expected, result);
        });
    }

    @Test
    public void testJSON() {
        int port = portCounter.next();
        String localhost = localhost(port);
        Handler handler = request -> JSONResponse.ok("{ values: [1, 2, 3] }");
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
        Router router = Router.of(req -> TextResponse.notFound(expectedNotFound))
                .get("/0", request -> TextResponse.ok(expected0))
                .get("/1", request -> TextResponse.ok(expected1));
        Jetty.use(port, router, server -> {
            assertEquals(expected0, Unirest.post(localhost + "/0").asString().getBody());
            assertEquals(expected1, Unirest.post(localhost + "/1").asString().getBody());
            assertEquals(expectedNotFound,
                    Unirest.post(localhost + "/2").asString().getBody());
        });
    }

    @Test
    public void testPost() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected = "Ok.";
        Jetty.use(port, request -> {
            assertEquals(RequestMethod.POST, request.getMethod());
            return TextResponse.ok(expected);
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
            return TextResponse.ok(expected);
        }, server -> {
            GetRequest request = Unirest.get(localhost);
            System.out.println(request.getHeaders().size());
            String result = request.asString().getBody();
            assertEquals(expected, result);
        });
    }

    @Test
    public void testXML() {
        int port = portCounter.next();
        String localhost = localhost(port);
        final String expected = "<awful>truly</awful>";
        Jetty.use(port, request -> XMLResponse.ok(expected), server -> {
            assertEquals(expected, Unirest.get(localhost).asString().getBody());
        });
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
