package org.hydrogen.jetty;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.hydrogen.Handler;
import org.hydrogen.Request;
import org.hydrogen.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HandlerWrapper extends AbstractHandler {
    private final Handler handler;

    public HandlerWrapper(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(String target,
            org.eclipse.jetty.server.Request baseRequest,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) throws IOException {
        Request request = new Request(target);
        Response response = handler.handle(request);
        response.accept(new ResponseAdapterImpl(httpResponse));
        baseRequest.setHandled(true);
    }
}
