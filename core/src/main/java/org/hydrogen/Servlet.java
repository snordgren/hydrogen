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
        response.accept(new ResponseBodyWriter(resp));
    }

    /**
     * Converts a Hydrogen response into a HttpServletResponse.
     *
     * @author Silas Nordgren
     */
    private static class ResponseBodyWriter implements ResponseAdapter<HttpServletResponse> {
        private final HttpServletResponse servletResponse;

        public ResponseBodyWriter(HttpServletResponse servletResponse) {
            this.servletResponse = servletResponse;
        }

        @Override
        public HttpServletResponse html(HTMLResponse response) {
            return writeTextualResponse(response);
        }

        @Override
        public HttpServletResponse json(JSONResponse response) {
            return writeTextualResponse(response);
        }

        @Override
        public HttpServletResponse text(TextResponse response) {
            return writeTextualResponse(response);
        }

        private HttpServletResponse writeTextualResponse(TextualResponse response) {
            servletResponse.setContentType(response.getContentType().getText());
            servletResponse.setStatus(response.getStatusCode().getNumber());
            if (response.getBytes().length > 0) {
                ExceptionUtils.run(() -> {
                    ServletOutputStream out = servletResponse.getOutputStream();
                    out.write(response.getBytes());
                    out.flush();
                    out.close();
                });
            }
            return servletResponse;
        }

        @Override
        public HttpServletResponse xml(XMLResponse response) {
            return writeTextualResponse(response);
        }
    }

}
