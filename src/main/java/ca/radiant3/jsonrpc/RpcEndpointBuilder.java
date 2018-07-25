package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.protocol.jsonrpc2.JsonRpc2Endpoint;
import ca.radiant3.jsonrpc.server.InvocationHandler;
import ca.radiant3.jsonrpc.server.InvokeByReflection;

public class RpcEndpointBuilder {

    private InvocationHandler invocationHandler;

    public RpcEndpointBuilder(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public static <T> RpcEndpointBuilder byReflection(Class<? super T> publishedInterface, T implementation) {
        InvokeByReflection invocationHandler = new InvokeByReflection(publishedInterface, implementation);
        return new RpcEndpointBuilder(invocationHandler);
    }

    public RpcEndpoint create() {
        return new JsonRpc2Endpoint(invocationHandler);
    }
}
