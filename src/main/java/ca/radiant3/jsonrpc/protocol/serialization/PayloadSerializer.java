package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.json.InvocationJson;
import ca.radiant3.jsonrpc.json.ResponseJson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PayloadSerializer {
    void serialize(InvocationJson invocation, OutputStream out) throws IOException;
    void serialize(ResponseJson response, OutputStream out) throws IOException;

    InvocationJson readInvocation(InputStream json);
    ResponseJson readResponse(InputStream json);
}
