package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.RpcService;
import ca.radiant3.jsonrpc.protocol.ExceptionMapper;
import ca.radiant3.jsonrpc.protocol.InvocationPayload;
import ca.radiant3.jsonrpc.protocol.ResponsePayload;
import ca.radiant3.jsonrpc.protocol.serialization.PayloadSerializer;
import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;
import ca.radiant3.jsonrpc.server.InvocationHandler;

import java.util.concurrent.CompletableFuture;

public class JsonRpc2Service implements RpcService {

    private PayloadSerializer serializer = new GsonPayloadSerializer();
    private ExceptionMapper exceptions = new JsonRpc2ExceptionsMapper();
    private JsonRpc2Protocol protocol;

    public JsonRpc2Service(InvocationHandler handler) {
        this.protocol = new JsonRpc2Protocol(handler, serializer, exceptions);
    }

    @Override
    public CompletableFuture<ResponsePayload> invoke(InvocationPayload payload) {
        return CompletableFuture.supplyAsync(() -> protocol.invoke(payload));
    }
}
