package org.hydrogen;

import org.hydrogen.util.ExceptionUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servlet extends HttpServlet {
    private final Handler handler;

    public Servlet(Handler handler) {
        this.handler = handler;
    }

    private Map<String, String> extractHeaders(HttpServletRequest req) {
        Enumeration<String> headerNames = req.getHeaderNames();
        List<String> headerNameList = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            headerNameList.add(headerNames.nextElement());
        }
        Map<String, String> headerMap = new HashMap<>();
        headerNameList.forEach(headerName -> {
            String value = req.getHeader(headerName);
            headerMap.put(headerName, value);
        });
        return headerMap;
    }

    private String appendQueryString(String url, String queryString) {
        if (queryString != null && !queryString.isEmpty()) {
            return "?" + queryString;
        } else return url;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestMethod method = RequestMethod.valueOf(req.getMethod());
        String url = appendQueryString(req.getRequestURI(), req.getQueryString());
        Map<String, String> headers = extractHeaders(req);
        InputStream body = req.getInputStream();
        Request request = new Request(method, url, headers, body);
        Response response = handler.handle(request);
        resp.setContentType(response.getContentType().getText());
        resp.setStatus(response.getStatusCode().getNumber());
        if (response.getBody().length > 0) {
            ExceptionUtils.run(() -> {
                ServletOutputStream out = resp.getOutputStream();
                out.write(response.getBody());
                out.flush();
                out.close();
            });
        }
        response.getHeaders().forEach(resp::setHeader);
    }
}
