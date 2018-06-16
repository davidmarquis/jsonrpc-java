package ca.radiant3.jsonrpc.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface InvocationPayload {
    String mimeType();
    void writeTo(OutputStream out) throws IOException;
    InputStream inputStream();

    static InvocationPayload from(InputStream input, String mimeType) {
        return new InvocationPayload() {
            @Override
            public String mimeType() {
                return mimeType;
            }

            @Override
            public void writeTo(OutputStream out) throws IOException {
                input.transferTo(out);
            }

            @Override
            public InputStream inputStream() {
                return input;
            }
        };
    }
}
