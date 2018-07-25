package ca.radiant3.jsonrpc.server;

import ca.radiant3.jsonrpc.RpcEndpoint;
import ca.radiant3.jsonrpc.protocol.jsonrpc2.JsonRpc2Endpoint;

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
