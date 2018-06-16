package ca.radiant3.jsonrpc.protocol.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface PayloadSerializer {

    void serialize(InvocationJson invocation, OutputStream out);
    void serialize(BatchJson batch, OutputStream out);
    void serialize(ResponseJson response, OutputStream out) throws IOException;

    InvocationJson readInvocation(InputStream json);

    BatchJson deserializeBatch(InputStream json);
}
