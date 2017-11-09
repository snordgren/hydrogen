package org.hydrogen;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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

    private <T> Map<String, T> extractEnumMap(
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

    private Request buildHydrogenRequest(HttpServletRequest req) throws IOException {
        RequestMethod method = RequestMethod.valueOf(req.getMethod());
        String url = req.getRequestURI();
        Map<String, String> queryParams = buildQueryParamMap(req.getQueryString());
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
        return new Request(method, url, queryParams, headers, body, session);
    }

    private Map<String, String> buildQueryParamMap(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyMap();
        } else {
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
            throws ServletException, IOException {
        Request request = buildHydrogenRequest(req);
        Response response = handler.handle(request);
        response.getSession().ifPresent(session ->
                applyResponseSession(session, req.getSession()));

        response.getHeaders().forEach(resp::addHeader);
        resp.setContentType(response.getContentType().getText());
        resp.setStatus(response.getStatusCode().getNumber());

        // NOTE: Once we start writing to the body, we can no longer add headers.
        if (response.getBody().length > 0) {
            ServletOutputStream out = resp.getOutputStream();
            out.write(response.getBody());
            out.flush();
            out.close();
        }
    }
}
