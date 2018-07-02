package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.protocol.InvocationPayload;
import ca.radiant3.jsonrpc.protocol.ResponsePayload;

import java.util.concurrent.CompletableFuture;

public interface RpcService {

    CompletableFuture<ResponsePayload> invoke(InvocationPayload invocation);
}
