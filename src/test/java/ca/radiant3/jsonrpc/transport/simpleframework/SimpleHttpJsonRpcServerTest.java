package ca.radiant3.jsonrpc.transport.simpleframework;

import ca.radiant3.jsonrpc.RpcServiceBuilder;
import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.client.JsonRpcClient;
import ca.radiant3.jsonrpc.json.InvocationJson;
import ca.radiant3.jsonrpc.json.ResponseJson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SimpleHttpJsonRpcServerTest {

    SimpleHttpJsonRpcServer server = new SimpleHttpJsonRpcServer(8777);
    JsonRpcClient client = JsonRpcClient.create("http://localhost:8777/echo.json");

    @Before
    public void startServer() throws IOException {
        server.bind("/echo.json", RpcServiceBuilder.byReflection(MyService.class, new MyService()).create());
        server.start();
    }

    @After
    public void stopServer() throws IOException {
        server.stop();
    }

    @Test
    public void roundTrip() throws Exception {
        CompletableFuture<ResponseJson> response = client.invoke(
                new InvocationJson().withJsonrpc("2.0")
                                    .withMethod("join")
                                    .withParameters(
                                            Value.of("Hello"),
                                            Value.of("World!")
                                    ));

        assertThat(response.get().as(String.class), equalTo("Hello World!"));
    }

    public static class MyService {
        public String join(String value1, String value2) {
            return String.join(" ", List.of(value1, value2));
        }
    }
}