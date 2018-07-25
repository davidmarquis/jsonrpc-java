package ca.radiant3.jsonrpc.transport.http.client;

import ca.radiant3.jsonrpc.client.RemoteRpcService;
import ca.radiant3.jsonrpc.protocol.payload.InvocationJson;
import ca.radiant3.jsonrpc.protocol.payload.ResponseJson;
import ca.radiant3.jsonrpc.protocol.serialization.PayloadSerializer;
import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HttpJsonRpcClient implements RemoteRpcService, Closeable {
    private final String url;
    private final PayloadSerializer serializer = new GsonPayloadSerializer();
    private final CloseableHttpClient client;

    public HttpJsonRpcClient(String url) {
        this.url = url;
        this.client = HttpClientBuilder.create().build();
    }

    public static HttpJsonRpcClient create(String endpointUrl) {
        return new HttpJsonRpcClient(endpointUrl);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Override
    public CompletableFuture<ResponseJson> invoke(InvocationJson invocation) {
        return CompletableFuture.supplyAsync(() -> {
            HttpPost post = new HttpPost(url);

            try {
                ByteArrayOutputStream body = new ByteArrayOutputStream();
                serializer.serialize(invocation, body);

                HttpEntity entity = new ByteArrayEntity(body.toByteArray(), ContentType.APPLICATION_JSON);
                post.setEntity(entity);

                try (CloseableHttpResponse response = client.execute(post)) {
                    return serializer.readResponse(response.getEntity().getContent());
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not invoke remote procedure", e);
            }
        });
    }
}
