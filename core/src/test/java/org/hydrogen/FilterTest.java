package org.hydrogen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FilterTest {
    @Test
    public void test() {
        Filter filter = handler -> req -> Response.ok().text("Yes.");
        Handler handler = filter.apply(req -> Response.notFound().text("No."));
        Response result = handler.handle(Request.getLocalhost(8080));
        assertEquals("Yes.", result.getBodyAsString());
        assertEquals(Status.OK, result.getStatus());
    }
}
