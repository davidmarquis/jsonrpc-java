package ca.radiant3.jsonrpc.transport.simpleframework;

import ca.radiant3.jsonrpc.protocol.jsonrpc2.JsonRpc2Service;
import ca.radiant3.jsonrpc.server.InvokeByReflection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SimpleHttpJsonRpcServerTest {

    SimpleHttpJsonRpcServer server = new SimpleHttpJsonRpcServer(8777);

    @Before
    public void startServer() throws IOException {
        server.bind("/echo.json", new JsonRpc2Service(new InvokeByReflection(MyService.class, new MyService())));
        server.start();
    }

    @After
    public void stopServer() throws IOException {
        server.stop();
    }

    @Test
    public void test() throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost("http://localhost:8777/echo.json");
            post.addHeader("Content-Type", "application/json");

            //language=JSON
            String body = "{\"jsonrpc\": \"2.0\", \"id\": 5441, \"method\": \"echo\", \"params\": {\"value\": \"Hello World!\"}}";
            HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
            post.setEntity(entity);
            try (CloseableHttpResponse response = client.execute(post)) {
                JsonObject result = new JsonParser().parse(new InputStreamReader(response.getEntity().getContent())).getAsJsonObject();
                System.out.println("result = " + result);
                assertThat(result.get("jsonrpc").getAsString(), is("2.0"));
//                assertThat(result.get("id").getAsInt(), is(5441));
                assertThat(result.get("result").getAsString(), is("Hello World!"));
            }
        }
    }

    public static class MyService {

        public String echo(String value) {
            return value;
        }
    }
}