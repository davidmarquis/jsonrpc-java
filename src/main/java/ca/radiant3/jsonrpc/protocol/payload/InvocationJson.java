package ca.radiant3.jsonrpc.protocol.payload;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Value;

public class InvocationJson {
    private static final String V2 = "2.0";

    private String jsonrpc;
    private String method;
    private ParametersJson params = ParametersJson.none();

    public static InvocationJson of(String method) {
        return new InvocationJson().withJsonrpc(V2).withMethod(method);
    }

    public static InvocationJson of(Invocation invocation) {
        return InvocationJson.of(invocation.getMethodName())
                             .withParameters(ParametersJson.of(invocation.getArguments()));
    }

    public InvocationJson withJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
        return this;
    }

    public InvocationJson withMethod(String method) {
        this.method = method;
        return this;
    }

    public InvocationJson withParameters(ParametersJson params) {
        this.params = params;
        return this;
    }

    public InvocationJson withParameters(Value... values) {
        this.params = ParametersJson.unnamed(values);
        return this;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public ParametersJson getParams() {
        return params;
    }

    public Invocation toInvocation() {
        Invocation invocation = new Invocation(method);

        params.addTo(invocation.getArguments());

        return invocation;
    }

    public boolean hasParameters() {
        return params.count() > 0;
    }
}
