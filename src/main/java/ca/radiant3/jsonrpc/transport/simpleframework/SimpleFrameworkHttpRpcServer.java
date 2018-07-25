package ca.radiant3.jsonrpc.transport.simpleframework;

import ca.radiant3.jsonrpc.RpcEndpoint;
import ca.radiant3.jsonrpc.protocol.InvocationPayload;
import ca.radiant3.jsonrpc.transport.RpcHttpServer;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SimpleFrameworkHttpRpcServer implements RpcHttpServer {
    private static final Logger log = LoggerFactory.getLogger(SimpleFrameworkHttpRpcServer.class);

    private final int port;
    private final EndpointRouter router;

    private SocketConnection server;

    public SimpleFrameworkHttpRpcServer(int port) {
        this.port = port;
        this.router = new EndpointRouter();
    }

    public void start() throws IOException {
        server = new SocketConnection(new ContainerSocketProcessor(router));
        SocketAddress address = new InetSocketAddress(port);

        server.connect(address);
    }

    public void stop() throws IOException {
        server.close();
    }

    @Override
    public void close() throws IOException {
        stop();
    }

    public void bind(URI path, RpcEndpoint target) {
        router.bind(path, new RpcHandler(target));
    }

    private static class EndpointRouter implements Container {
        private Map<URI, RpcHandler> routes = new HashMap<>();

        public void bind(URI path, RpcHandler target) {
            routes.put(path, target);
        }

        @Override
        public void handle(Request req, Response resp) {
            URI path = URI.create(req.getPath().getPath());
            RpcHandler handler = routes.get(path);
            if (handler != null) {
                handler.handle(req, resp);
            }
        }
    }

    private static class RpcHandler implements Container {
        private final RpcEndpoint service;

        public RpcHandler(RpcEndpoint service) {
            this.service = service;
        }

        @Override
        public void handle(Request req, Response resp) {
            try {
                String contentType = req.getContentType().getType();
                try (InputStream in = req.getInputStream()) {
                    InvocationPayload payload = InvocationPayload.from(in, contentType);

                    service.invoke(payload).thenAccept(response -> {
                        resp.setContentType(response.mimeType());
                        try {
                            OutputStream body = resp.getOutputStream();
                            response.writeTo(body);
                            body.flush();

                            resp.close();
                        } catch (IOException e) {
                            log.error("Could not write response to HTTP client", e);
                        }
                    });
                }
            } catch (IOException e) {
                log.error("Could not read contents of HTTP request", e);
            }
        }
    }
}
