package ca.radiant3.jsonrpc.client;

import ca.radiant3.jsonrpc.protocol.payload.InvocationJson;
import ca.radiant3.jsonrpc.protocol.payload.ResponseJson;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface RemoteRpcService {

    CompletableFuture<ResponseJson> invoke(InvocationJson invocation) throws IOException;
}
