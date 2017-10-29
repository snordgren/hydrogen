package org.hydrogen.jetty;

import org.hydrogen.ResponseAdapter;
import org.hydrogen.TextResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseAdapterImpl implements ResponseAdapter<HttpServletResponse> {
    private final HttpServletResponse servletResponse;

    public ResponseAdapterImpl(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    @Override
    public HttpServletResponse text(TextResponse response) {
        servletResponse.setContentType(response.getContentType().getText());
        servletResponse.setStatus(response.getStatusCode().getNumber());
        if (response.getBytes().length > 0) {
            try {
                ServletOutputStream out = servletResponse.getOutputStream();
                out.write(response.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return servletResponse;
    }
}
