package ca.radiant3.jsonrpc.json;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Value;

public class InvocationJson {
    private String jsonrpc;
    private String method;
    private ParametersJson params = ParametersJson.none();

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
        this.params = ParametersJson.of(values);
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

        params.addTo(invocation.getParameters());

        return invocation;
    }

    public boolean hasParameters() {
        return params.count() > 0;
    }
}
