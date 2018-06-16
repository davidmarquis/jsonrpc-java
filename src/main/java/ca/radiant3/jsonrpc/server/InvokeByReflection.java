package ca.radiant3.jsonrpc.server;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.InvocationHandler;
import ca.radiant3.jsonrpc.Result;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class InvokeByReflection implements InvocationHandler {

    private final Class<?> publishedInterface;
    private final Object target;

    private final List<Signature> signatures;

    public <T> InvokeByReflection(Class<? super T> publishedInterface, T implementation) {
        this.publishedInterface = publishedInterface;
        this.target = implementation;

        this.signatures = Arrays.stream(publishedInterface.getMethods())
                                .map(Signature::of)
                                .collect(toList());
    }

    @Override
    public Result handle(Invocation invocation) throws Exception {
        Signature method = resolveMethod(invocation);
        return Result.of(method.invoke(target, invocation), method.getReturnType());
    }

    private Signature resolveMethod(Invocation invocation) throws NoSuchMethodException {
        String methodName = invocation.getMethodName();

        return signatures.stream()
                     .filter(it -> it.matches(invocation))
                     .findFirst()
                     .orElseThrow(() -> new NoSuchMethodException(methodName));
    }
}
