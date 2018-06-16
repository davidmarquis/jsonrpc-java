package ca.radiant3.jsonrpc.protocol;

import java.util.concurrent.CompletableFuture;

public interface RpcService {

    CompletableFuture<ResponsePayload> invoke(InvocationPayload invocation);
}
