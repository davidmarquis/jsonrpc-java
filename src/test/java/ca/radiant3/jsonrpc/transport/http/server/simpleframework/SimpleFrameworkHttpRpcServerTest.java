package ca.radiant3.jsonrpc.transport.http.server.simpleframework;

import ca.radiant3.jsonrpc.RpcEndpointBuilder;
import ca.radiant3.jsonrpc.client.RpcClientProxy;
import ca.radiant3.jsonrpc.transport.http.client.HttpJsonRpcClient;
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

    MyRemoteService client = RpcClientProxy.createFor(MyRemoteService.class, HttpJsonRpcClient.create("http://localhost:8777/echo.json"));

    @Before
    public void startServer() throws IOException {
        server.bind(URI.create("/echo.json"),
                    RpcEndpointBuilder.byReflection(MyRemoteService.class, new MyRemoteServiceServer()).create()
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

    public interface MyRemoteService {
        String join(String value1, String value2);
    }

    public static class MyRemoteServiceServer implements MyRemoteService {
        public String join(String value1, String value2) {
            return String.join(" ", List.of(value1, value2));
        }
    }
}