package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.protocol.jsonrpc2.JsonRpc2Service;
import ca.radiant3.jsonrpc.server.InvocationHandler;
import ca.radiant3.jsonrpc.server.InvokeByReflection;

public class RpcServiceBuilder {

    private InvocationHandler invocationHandler;

    public RpcServiceBuilder(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public static <T> RpcServiceBuilder byReflection(Class<? super T> publishedInterface, T implementation) {
        return new RpcServiceBuilder(new InvokeByReflection(publishedInterface, implementation));
    }

    public RpcService create() {
        return new JsonRpc2Service(invocationHandler);
    }
}
