package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.json.InvocationJson;
import ca.radiant3.jsonrpc.json.ResponseJson;
import ca.radiant3.jsonrpc.protocol.*;
import ca.radiant3.jsonrpc.protocol.serialization.PayloadSerializer;
import ca.radiant3.jsonrpc.server.InvocationHandler;

import java.io.IOException;
import java.io.OutputStream;

public class JsonRpc2Protocol {
    public static final String VERSION = "2.0";
    public static final String MIME_TYPE = "application/json";

    private final InvocationHandler handler;
    private final PayloadSerializer serializer;
    private final ExceptionMapper exceptionMapper;

    public JsonRpc2Protocol(InvocationHandler handler, PayloadSerializer serializer, ExceptionMapper exceptionMapper) {
        this.handler = handler;
        this.serializer = serializer;
        this.exceptionMapper = exceptionMapper;
    }

    public ResponsePayload invoke(InvocationPayload payload) {
        ResponseJson response;
        try {
            if (!MIME_TYPE.equals(payload.mimeType())) {
                response = ResponseJson.unboundError(Errors.invalidMimeType(payload.mimeType()));
            } else {
                InvocationJson json = serializer.readInvocation(payload.inputStream());
                response = invoke(json);
            }
        } catch (Exception e) {
            response = ResponseJson.unboundError(Errors.invalidFormat());
        }
        return new JsonResponsePayload(response);
    }

    private ResponseJson invoke(InvocationJson json) throws Exception {
        ResponseJson response = ResponseJson.to(json);
        try {
            Invocation invocation = json.toInvocation();
            validateVersion(json);

            return response.success(handler.handle(invocation));
        } catch (Exception e) {
            return response.error(exceptionMapper.toError(e));
        }
    }

    private void validateVersion(InvocationJson json) {
        if (!VERSION.equals(json.getJsonrpc())) {
            throw new InvalidProtocolVersion(json.getJsonrpc());
        }
    }

    public class JsonResponsePayload implements ResponsePayload {
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

        public ResponseJson getResponse() {
            return response;
        }
    }
}
