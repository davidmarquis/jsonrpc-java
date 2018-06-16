package ca.radiant3.jsonrpc.transport.simpleframework;

import ca.radiant3.jsonrpc.protocol.InvocationPayload;
import ca.radiant3.jsonrpc.protocol.RpcService;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class SimpleHttpJsonRpcServer implements Closeable {

    private final int port;
    private final ServiceRouter router;

    private SocketConnection server;

    public SimpleHttpJsonRpcServer(int port) {
        this.port = port;
        this.router = new ServiceRouter();
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

    public void bind(String path, RpcService target) {
        router.bind(path, new RpcHandler(target));
    }

    private static class ServiceRouter implements Container {
        private Map<String, RpcHandler> routes = new HashMap<>();

        public void bind(String path, RpcHandler target) {
            routes.put(path, target);
        }

        @Override
        public void handle(Request req, Response resp) {
            String path = req.getPath().getPath();
            RpcHandler handler = routes.get(path);
            if (handler != null) {
                handler.handle(req, resp);
            }
        }
    }

    private static class RpcHandler implements Container {
        private final RpcService service;

        public RpcHandler(RpcService service) {
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
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
