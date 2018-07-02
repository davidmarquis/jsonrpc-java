package ca.radiant3.jsonrpc.client;

import ca.radiant3.jsonrpc.protocol.serialization.InvocationJson;
import ca.radiant3.jsonrpc.protocol.serialization.ResponseJson;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface RemoteService {

    CompletableFuture<ResponseJson> invoke(InvocationJson invocation) throws IOException;
}
