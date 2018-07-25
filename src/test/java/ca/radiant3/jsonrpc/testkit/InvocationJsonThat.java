package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.protocol.payload.InvocationJson;
import ca.radiant3.jsonrpc.protocol.payload.ParametersJson;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

public class InvocationJsonThat {
    public static Matcher<InvocationJson> hasSameState(InvocationJson other) {
        return allOf(
                hasJsonrpc(other.getJsonrpc()),
                hasMethod(other.getMethod()),
                hasParameters(ParametersJsonThat.hasSameState(other.getParams()))
        );
    }


    public static Matcher<InvocationJson> hasJsonrpc(String jsonrpc) {
        return hasProperty("jsonrpc", equalTo(jsonrpc));
    }
    public static Matcher<InvocationJson> hasMethod(String method) {
        return hasProperty("method", equalTo(method));
    }

    public static Matcher<InvocationJson> hasParameters(Matcher<ParametersJson> matching) {
        return hasProperty("params", matching);
    }
}
