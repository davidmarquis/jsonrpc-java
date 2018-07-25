package ca.radiant3.jsonrpc.server;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Value;

public interface InvocationHandler {
    Value handle(Invocation invocation) throws Exception;
}
