package org.hydrogen;

import org.hydrogen.jetty.ResponseAdapterImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Servlet extends HttpServlet {
    private final Handler handler;

    public Servlet(Handler handler) {
        this.handler = handler;
    }

    private String[] extractHeaders(Enumeration<String> headers) {
        List<String> headerList = new ArrayList<>();
        while (headers.hasMoreElements()) {
            headerList.add(headers.nextElement());
        }
        return headerList.toArray(new String[headerList.size()]);
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
        String[] headers = extractHeaders(req.getHeaderNames());
        InputStream body = req.getInputStream();
        Request request = new Request(method, url, headers, body);
        Response response = handler.handle(request);
        response.accept(new ResponseAdapterImpl(resp));
    }
}
