package org.hydrogen;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServletTest {
    @Test
    public void testUnicodePath() throws IOException {
        String encodedURL = URLEncoder.encode("现在", "UTF-8");
        String encodedQueryString = URLEncoder.encode("什么=这个", "UTF-8");
        assertTrue(encodedURL.contains("%"));
        assertTrue(encodedQueryString.contains("%"));

        Enumeration<String> emptyEnum = mock(Enumeration.class);
        when(emptyEnum.hasMoreElements()).thenReturn(false);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttributeNames()).thenReturn(emptyEnum);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeaderNames()).thenReturn(emptyEnum);
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getQueryString()).thenReturn(encodedQueryString);
        when(httpServletRequest.getRequestURI()).thenReturn(encodedURL);
        when(httpServletRequest.getSession()).thenReturn(session);

        Request request = Servlet.mapRequest(httpServletRequest);
        assertEquals("现在", request.getUrl());
        assertEquals("这个", request.getQueryParam("什么"));
    }
}
