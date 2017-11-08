package org.hydrogen.jetty;

import org.hydrogen.HTMLResponse;
import org.hydrogen.JSONResponse;
import org.hydrogen.ResponseAdapter;
import org.hydrogen.TextResponse;
import org.hydrogen.TextualResponse;
import org.hydrogen.XMLResponse;
import org.hydrogen.util.ExceptionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
