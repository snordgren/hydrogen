package org.hydrogen;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Servlet implementation that manages the flow of information between the
 * Java servlet API and the handler.
 * <p>
 * This class is non-final to allow Hydrogen to be ran in servlet containers.
 * To create your own servlet component, extend this class and pass your
 * handler in the constructor.
 */
public class Servlet extends HttpServlet {
    private final Handler handler;

    public Servlet(Handler handler) {
        this.handler = handler;
    }

    private void applyResponseSession(Session session, HttpSession httpSession) {
        if (!session.isValid()) {
            httpSession.invalidate();
        }

        Set<String> attrNames = session.getAttributeNames();
        Set<String> presentNames = extractEnumSet(httpSession.getAttributeNames());
        presentNames.forEach(name -> {
            if (!attrNames.contains(name)) {
                httpSession.removeAttribute(name);
            }
        });
        attrNames.forEach(name ->
                httpSession.setAttribute(name, session.getAttribute(name)));
    }

    private static String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Map<String, T> extractEnumMap(
            Enumeration<String> enumeration,
            Function<String, T> accessor) {
        Map<String, T> map = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            T value = accessor.apply(name);
            map.put(name, value);
        }
        return map;
    }

    private Set<String> extractEnumSet(Enumeration<String> enumeration) {
        Set<String> set = new HashSet<>();
        while (enumeration.hasMoreElements()) {
            set.add(enumeration.nextElement());
        }
        return set;
    }

    private static Map<String, String> buildQueryParamMap(HttpServletRequest req) {
        String encodedStr = req.getQueryString();
        if (encodedStr == null || encodedStr.isEmpty()) {
            return Collections.emptyMap();
        } else {
            String str = decode(encodedStr);
            if (str.startsWith("&")) {
                str = str.substring(1);
            }
            Map<String, String> map = new HashMap<>();
            String[] assignments = str.split("&");
            for (String assignment : assignments) {
                String[] parts = assignment.split("=");
                String key = parts[0];
                String value = parts[1];

                // TODO Handle what happens if the length is invalid.

                map.put(key, value);
            }
            return map;
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Request request = mapRequest(req);
        Response response = handler.handle(request);
        response.getSession().ifPresent(session ->
                applyResponseSession(session, req.getSession()));

        response.getHeaders().forEach(resp::addHeader);
        resp.setContentType(response.getContentType().getText());
        resp.setStatus(response.getStatus().getNumber());

        // NOTE: Once we start writing to the body, we can no longer add headers.
        if (response.getBody().length > 0) {
            ServletOutputStream out = resp.getOutputStream();
            out.write(response.getBody());
            out.flush();
            out.close();
        }
    }

    public static Request mapRequest(HttpServletRequest req) throws IOException {
        RequestMethod method = RequestMethod.valueOf(req.getMethod());
        String url = decode(req.getRequestURI());
        Map<String, String> queryParams = buildQueryParamMap(req);
        Map<String, String> headers = extractEnumMap(req.getHeaderNames(),
                req::getHeader);
        InputStream body = req.getInputStream();

        HttpSession httpSession = req.getSession();
        Map<String, Object> sessionAttributes = extractEnumMap(
                httpSession.getAttributeNames(),
                httpSession::getAttribute);

        Session session = new Session(
                httpSession.getId(),
                httpSession.isNew(),
                true,
                sessionAttributes);
        return Request.builder()
                .method(method)
                .url(url)
                .queryParams(queryParams)
                .headers(headers)
                .body(body)
                .session(session)
                .build();
    }
}
