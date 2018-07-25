package ca.radiant3.jsonrpc.client;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Signature;
import ca.radiant3.jsonrpc.json.InvocationJson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

/**
 * Dynamic proxy that will intercept invocations and forward them to a remote RPC service.
 */
public class RpcClientProxy implements InvocationHandler {
    private static final int DEFAULT_TIMEOUT_SECS = 30;

    private final RemoteRpcService remoteRpcService;

    public RpcClientProxy(RemoteRpcService remoteRpcService) {
        this.remoteRpcService = remoteRpcService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = Signature.of(method).toInvocation(asList(args));

        return remoteRpcService.invoke(InvocationJson.of(invocation))
                               // todo: handle errors by throwing an Exception
                               .thenApply(response -> response.getResult().readAs(method.getReturnType()))
                               .get(DEFAULT_TIMEOUT_SECS, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createFor(Class<T> service, RemoteRpcService client) {
        return (T) Proxy.newProxyInstance(
                RpcClientProxy.class.getClassLoader(),
                new Class[]{service},
                new RpcClientProxy(client)
        );
    }
}
