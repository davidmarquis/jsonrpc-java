package ca.radiant3.jsonrpc;

public class Invocation {
    private String methodName;
    private Args arguments = new Args();

    public Invocation(String methodName) {
        this.methodName = methodName;
    }

    public static Invocation of(String method) {
        return new Invocation(method);
    }

    public String getMethodName() {
        return methodName;
    }

    public Invocation withArgument(Arg arg) {
        arguments.add(arg);
        return this;
    }

    public Args getArguments() {
        return arguments;
    }
}
