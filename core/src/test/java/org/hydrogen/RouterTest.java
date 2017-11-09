package org.hydrogen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RouterTest {
    private final String expectedContents = "Lorem ipsum dolor sit amet.";

    @Test
    public void testBasicRoute() {
        String expectedGet = "GET index";
        String expectedPost = "POST index";
        Router router = Router.builder()
                .notFound(req -> Response.notFound().text("404 Not Found"))
                .get("/", req -> Response.ok().text(expectedGet))
                .post("/", req -> Response.ok().text(expectedPost))
                .build();
        Request getIndex = Request.get("/");
        Request postIndex = Request.post("/");
        assertEquals(expectedGet, router.handle(getIndex).getBodyAsString());
        assertEquals(expectedPost, router.handle(postIndex).getBodyAsString());
    }

    @Test
    public void testHandlerGroup() {
        String expected = "Success!";
        Router user = Router.builder()
                .get("/", request -> Response.ok().text(expected))
                .build();
        Router router = Router.builder()
                .group("/user", user)
                .build();
        Response response = router.handle(new Request(RequestMethod.GET, "/user/"));
        assertEquals(expected, response.getBodyAsString());
    }

    @Test
    public void testStaticFiles() {
        Router router = Router.builder()
                .bind("public", new ClasspathDirectory(""))
                .build();
        Request request = new Request(RequestMethod.GET, "/public/TestFile.txt");
        Response response = router.handle(request);
        assertEquals(expectedContents, response.getBodyAsString());
    }

    @Test
    public void testLocalhost() {
        Router router = Router.builder()
                .bind("/public", new ClasspathDirectory(""))
                .get("wonderfully/long", request -> Response.ok().text("No."))
                .get("", request -> Response.ok().text("Yes."))
                .build();
        router.handle(new Request(RequestMethod.GET, ""));
    }

    @Test
    public void testRootStaticDirectory() {
        Router router = Router.builder()
                .bind("", new ClasspathDirectory("/"))
                .get("/", req -> Response.ok().text("Yes."))
                .build();

        assertEquals("Yes.", router.handle(new Request(RequestMethod.GET, ""))
                .getBodyAsString());

        assertEquals(expectedContents, router.handle(
                new Request(RequestMethod.GET, "/TestFile.txt"))
                .getBodyAsString());

        assertEquals(expectedContents, router.handle(
                new Request(RequestMethod.GET, "/dir/TestFile.txt"))
                .getBodyAsString());
    }

}
