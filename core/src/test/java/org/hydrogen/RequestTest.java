package org.hydrogen;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;

public class RequestTest {
    @Test
    public void testImmutableMaps() {
        Map<String, String> mutableMap = new HashMap<>();
        Request request = Request.builder()
                .routeParam("initialMapsMustBeMutable", "true")
                .routeParams(mutableMap)
                .queryParams(mutableMap)
                .headers(mutableMap)
                .build();
        mutableMap.put("abc", "def");
        assertFalse(request.hasRouteParam("abc"));
        assertFalse(request.hasQueryParam("abc"));
        assertFalse(request.hasHeader("abc"));
    }
}
