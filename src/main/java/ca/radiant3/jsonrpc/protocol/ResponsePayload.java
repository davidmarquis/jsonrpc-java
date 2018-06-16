package ca.radiant3.jsonrpc.protocol;

import java.io.IOException;
import java.io.OutputStream;

public interface ResponsePayload {
    String mimeType();
    void writeTo(OutputStream out) throws IOException;
}
