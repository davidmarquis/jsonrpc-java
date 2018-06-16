package ca.radiant3.jsonrpc;

public interface InvocationHandler {

    Result handle(Invocation invocation) throws Exception;
}
