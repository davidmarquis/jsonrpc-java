package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.InvocationHandler;
import ca.radiant3.jsonrpc.Result;

public class StubInvocationHandler implements InvocationHandler {

    private Result result;

    public void responds(Result response) {
        this.result = response;
    }

    @Override
    public Result handle(Invocation invocation) throws Exception {
        return result;
    }
}
