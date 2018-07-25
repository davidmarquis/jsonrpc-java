package ca.radiant3.jsonrpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Signature {
    private final Method method;
    private List<DeclaredParameter> declaredParameters;

    private Signature(Method method) {
        this.method = method;
    }

    public static Signature of(Method method) {
        Signature signature = new Signature(method);
        signature.declaredParameters = Arrays.stream(method.getParameters())
                                             .map(DeclaredParameter::of)
                                             .collect(toList());
        return signature;
    }

    public String getMethodName() {
        return method.getName();
    }

    public List<DeclaredParameter> parameters() {
        return Collections.unmodifiableList(declaredParameters);
    }

    public Type getReturnType() {
        return method.getGenericReturnType();
    }

    public static Predicate<Signature> hasMethodName(String name) {
        return signature -> signature.getMethodName().equals(name);
    }

    public static Predicate<Signature> hasParameters(Args params) {
        return hasParameterCount(params.count())
                .and(parameterTypesAreCompatible(params));
    }

    private static Predicate<Signature> hasParameterCount(int count) {
        return signature -> signature.declaredParameters.size() == count;
    }

    private static Predicate<Signature> parameterTypesAreCompatible(Args params) {
        return signature -> {
            List<Arg> list = params.list();
            for (int i = 0; i < list.size(); i++) {
                Arg arg = list.get(i);
                DeclaredParameter declaredParam = signature.declaredParameters.get(i);
                if (!arg.getValue().isCompatibleWith(declaredParam.getType())) {
                    return false;
                }
            }
            return true;
        };
    }

    public Object invoke(Object target, Invocation invocation)
            throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, toArgs(method, invocation));
    }

    private Object[] toArgs(Method method, Invocation invocation) {
        List<Object> result = new ArrayList<>();

        List<Arg> args = invocation.getArguments().list();
        for (int i = 0; i < args.size(); i++) {
            Arg parameter = args.get(i);
            Parameter param = method.getParameters()[i];
            result.add(parameter.getValue().readAs(param.getType()));
        }
        return result.toArray();
    }

    public Invocation toInvocation(List<Object> args) {
        Invocation result = Invocation.of(getMethodName());
        for (int i = 0; i < args.size(); i++) {
            Object argument = args.get(i);
            DeclaredParameter parameter = declaredParameters.get(i);

            result.withParameter(parameter.forValue(argument));
        }
        return result;
    }

    public static class DeclaredParameter {
        private final Type type;
        private String name;

        public DeclaredParameter(Type type) {
            this.type = type;
        }

        public static DeclaredParameter of(Parameter parameter) {
            DeclaredParameter result = new DeclaredParameter(parameter.getParameterizedType());

            Named nameHint = parameter.getAnnotation(Named.class);
            if (nameHint != null) {
                result = result.named(nameHint.value());
            }
            return result;
        }

        public DeclaredParameter named(String name) {
            this.name = name;
            return this;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

        public Arg forValue(Object argument) {
            return Arg.of(Value.of(argument, type)).named(name);
        }
    }
}
