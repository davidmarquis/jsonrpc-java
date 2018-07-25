package ca.radiant3.jsonrpc.transport.simpleframework;

import ca.radiant3.jsonrpc.client.JsonRpcClient;
import ca.radiant3.jsonrpc.client.proxy.JsonRpcClientProxy;
import ca.radiant3.jsonrpc.server.RpcEndpointBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SimpleFrameworkHttpRpcServerTest {
    SimpleFrameworkHttpRpcServer server = new SimpleFrameworkHttpRpcServer(8777);
    MyService client = JsonRpcClientProxy.createFor(MyService.class, JsonRpcClient.create("http://localhost:8777/echo.json"));

    @Before
    public void startServer() throws IOException {
        server.bind(URI.create("/echo.json"),
                    RpcEndpointBuilder.byReflection(MyService.class, new MyServiceServer()).create()
        );
        server.start();
    }

    @After
    public void stopServer() throws IOException {
        server.stop();
    }

    @Test
    public void roundTrip() {
        String result = client.join("Hello", "World!");

        assertThat(result, equalTo("Hello World!"));
    }

    public interface MyService {
        String join(String value1, String value2);
    }

    public static class MyServiceServer implements MyService {
        public String join(String value1, String value2) {
            return String.join(" ", List.of(value1, value2));
        }
    }
}