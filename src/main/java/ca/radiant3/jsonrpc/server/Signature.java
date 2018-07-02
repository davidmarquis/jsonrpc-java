package ca.radiant3.jsonrpc.server;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Param;
import ca.radiant3.jsonrpc.Parameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Signature {
    private final Method method;
    private List<DeclaredParameter> parameters;

    private Signature(Method method) {
        this.method = method;
    }

    public static Signature of(Method method) {
        Signature signature = new Signature(method);
        signature.parameters = Arrays.stream(method.getParameters())
                                     .map(DeclaredParameter::of)
                                     .collect(toList());
        return signature;
    }

    public String methodName() {
        return method.getName();
    }

    public List<DeclaredParameter> parameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Type getReturnType() {
        return method.getGenericReturnType();
    }

    public boolean matches(Invocation invocation) {
        return methodName().equals(invocation.getMethodName());
    }

    public Object invoke(Object target, Invocation invocation)
            throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, toArgs(method, invocation));
    }

    private Object[] toArgs(Method method, Invocation invocation) {
        Parameters parameters = invocation.getParameters();
        List<Object> result = new ArrayList<>();

        List<Param> parameterList = parameters.list();
        for (int i = 0; i < parameterList.size(); i++) {
            Param parameter = parameterList.get(i);
            Parameter param = method.getParameters()[i];
            result.add(parameter.getValue().readAs(param.getType()));
        }
        return result.toArray();
    }

    public static class DeclaredParameter {
        private final Class<?> type;
        private String name;

        public DeclaredParameter(Class<?> type) {
            this.type = type;
        }

        public static DeclaredParameter of(Parameter parameter) {
            DeclaredParameter declared = new DeclaredParameter(parameter.getType());
            return declared;
        }

        public DeclaredParameter named(String name) {
            this.name = name;
            return this;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }
    }
}
