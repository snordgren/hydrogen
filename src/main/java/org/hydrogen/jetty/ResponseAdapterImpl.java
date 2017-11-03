package org.hydrogen.jetty;

import org.hydrogen.HTMLResponse;
import org.hydrogen.JSONResponse;
import org.hydrogen.ResponseAdapter;
import org.hydrogen.TextResponse;
import org.hydrogen.TextualResponse;
import org.hydrogen.XMLResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Converts a Hydrogen response into a HttpServletResponse.
 *
 * @author Silas Nordgren
 */
public class ResponseAdapterImpl implements ResponseAdapter<HttpServletResponse> {
    private final HttpServletResponse servletResponse;

    public ResponseAdapterImpl(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    private HttpServletResponse content(TextualResponse response) {
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

    @Override
    public HttpServletResponse html(HTMLResponse response) {
        return content(response);
    }

    @Override
    public HttpServletResponse json(JSONResponse response) {
        return content(response);
    }

    @Override
    public HttpServletResponse text(TextResponse response) {
        return content(response);
    }

    @Override
    public HttpServletResponse xml(XMLResponse response) {
        return content(response);
    }
}
