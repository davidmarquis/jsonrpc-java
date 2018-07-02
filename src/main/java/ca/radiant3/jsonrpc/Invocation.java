package ca.radiant3.jsonrpc;

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

    public Invocation withParameter(Param param) {
        parameters.addParameter(param);
        return this;
    }

    public Parameters getParameters() {
        return parameters;
    }
}
