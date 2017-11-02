package org.hydrogen.jetty;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.hydrogen.Handler;
import org.hydrogen.Request;
import org.hydrogen.RequestMethod;
import org.hydrogen.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyHandler extends AbstractHandler {
    private final Handler handler;

    public JettyHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(String target,
            org.eclipse.jetty.server.Request baseRequest,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) throws IOException {
        RequestMethod method = RequestMethod.valueOf(baseRequest.getMethod().toUpperCase());
        Request request = new Request(method, target);
        Response response = handler.handle(request);
        response.accept(new ResponseAdapterImpl(httpResponse));
        baseRequest.setHandled(true);
    }
}
