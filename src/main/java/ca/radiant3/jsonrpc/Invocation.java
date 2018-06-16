package ca.radiant3.jsonrpc;

import java.util.List;

public class Invocation {
    private String methodName;
    private Parameters parameters = new Parameters();

    public Invocation(String methodName) {
        this.methodName = methodName;
    }

    public static Invocation of(String method) {
        return new Invocation(method);
    }

    public String getMethodName() {
        return methodName;
    }

    public Invocation withParameter(Object parameter) {
        parameters.addParameter(new Param(Value.of(parameter)));
        return this;
    }

    public List<Param> getParameters() {
        return parameters.getParameters();
    }
}
