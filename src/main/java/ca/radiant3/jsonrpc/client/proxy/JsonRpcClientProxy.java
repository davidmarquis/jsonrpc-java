package ca.radiant3.jsonrpc.client.proxy;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Signature;
import ca.radiant3.jsonrpc.client.RemoteService;
import ca.radiant3.jsonrpc.json.InvocationJson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class JsonRpcClientProxy implements InvocationHandler {
    private static final int DEFAULT_TIMEOUT_SECS = 30;

    private final RemoteService remoteService;

    private int timeoutSeconds = DEFAULT_TIMEOUT_SECS;

    public JsonRpcClientProxy(RemoteService remoteService) {
        this.remoteService = remoteService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = Signature.of(method).toInvocation(asList(args));

        return remoteService.invoke(InvocationJson.of(invocation))
                            // todo: handle errors by throwing an Exception
                            .thenApply(response -> response.getResult().readAs(method.getReturnType()))
                            .get(timeoutSeconds, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createFor(Class<T> service, RemoteService client) {
        return (T) Proxy.newProxyInstance(
                JsonRpcClientProxy.class.getClassLoader(),
                new Class[]{service},
                new JsonRpcClientProxy(client)
        );
    }
}
