package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.server.InvocationHandler;

public class StubInvocationHandler implements InvocationHandler {
    private Value returnValue;
    private Exception toThrow;

    public void returns(Value response) {
        this.returnValue = response;
    }

    public void throwing(Exception exception) {
        this.toThrow = exception;
    }

    @Override
    public Value handle(Invocation invocation) throws Exception {
        if (toThrow != null) {
            throw toThrow;
        }
        return returnValue;
    }
}
