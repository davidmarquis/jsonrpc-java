package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.InvocationHandler;
import ca.radiant3.jsonrpc.protocol.InvocationPayload;
import ca.radiant3.jsonrpc.protocol.ResponsePayload;
import ca.radiant3.jsonrpc.protocol.RpcService;
import ca.radiant3.jsonrpc.protocol.serialization.PayloadSerializer;
import ca.radiant3.jsonrpc.protocol.serialization.ResponseJson;
import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

public class JsonRpc2Service implements RpcService {

    private PayloadSerializer serializer = new GsonPayloadSerializer();
    private ExceptionMapper exceptions = new JsonRpc2ExceptionsMapper();
    private JsonRpc2Protocol protocol;

    public JsonRpc2Service(InvocationHandler handler) {
        this.protocol = new JsonRpc2Protocol(handler, exceptions);
    }

    @Override
    public CompletableFuture<ResponsePayload> invoke(InvocationPayload payload) {
        return CompletableFuture.supplyAsync(() -> invokeSync(payload))
                                .thenApply(this::toResponse);
    }

    private ResponseJson invokeSync(InvocationPayload payload) {
        return protocol.invoke(serializer.readInvocation(payload.inputStream()));
    }

    public ResponsePayload toResponse(ResponseJson response) {
        return new JsonResponsePayload(response);
    }

    private class JsonResponsePayload implements ResponsePayload {
        private final ResponseJson response;

        public JsonResponsePayload(ResponseJson response) {
            this.response = response;
        }

        @Override
        public String mimeType() {
            return "application/json";
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            serializer.serialize(response, out);
        }
    }
}
