package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Invocation;

import java.util.ArrayList;
import java.util.List;

public class InvocationJson {
    private String jsonrpc;
    private String method;
    private List<ParameterJson> params = new ArrayList<>();

    public InvocationJson withJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
        return this;
    }

    public InvocationJson withMethod(String method) {
        this.method = method;
        return this;
    }

    public InvocationJson addParameter(ParameterJson param) {
        this.params.add(param);
        return this;
    }

    public String getJsonRpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public List<ParameterJson> getParams() {
        return params;
    }

    public Invocation toInvocation() {
        Invocation invocation = new Invocation(method);

        params.stream()
              .map(ParameterJson::toParam)
              .forEach(invocation::withParameter);
        
        return invocation;
    }
}
