package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.InvocationHandler;
import ca.radiant3.jsonrpc.Result;
import ca.radiant3.jsonrpc.protocol.serialization.InvocationJson;
import ca.radiant3.jsonrpc.protocol.serialization.ResponseJson;

public class JsonRpc2Protocol {
    private final InvocationHandler handler;
    private final ExceptionMapper exceptionMapper;

    public JsonRpc2Protocol(InvocationHandler handler, ExceptionMapper exceptionMapper) {
        this.handler = handler;
        this.exceptionMapper = exceptionMapper;
    }

    public ResponseJson invoke(InvocationJson json) {
        Invocation invocation = toInvocation(json);

        ResponseJson response = ResponseJson.to(invocation);
        try {
            Result result = handler.handle(invocation);
            return response.success(result);
        } catch (Exception e) {
            return response.error(exceptionMapper.toError(e));
        }
    }

    private Invocation toInvocation(InvocationJson json) {
        return new Invocation(json.getMethod());
    }
}
