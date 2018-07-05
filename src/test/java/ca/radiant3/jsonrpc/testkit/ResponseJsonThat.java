package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.json.ErrorJson;
import ca.radiant3.jsonrpc.json.ResponseJson;
import org.hamcrest.Matcher;

import java.io.Serializable;

import static ca.radiant3.jsonrpc.testkit.ValueThat.isSameValue;
import static org.hamcrest.Matchers.*;

public class ResponseJsonThat {

    public static Matcher<ResponseJson> hasSameState(ResponseJson other) {
        return allOf(
                hasJsonRpc(other.getJsonrpc()),
                hasId(other.getId()),
                hasError(other.getError()),
                hasResult(other.getResult())
        );
    }

    public static Matcher<ResponseJson> hasJsonRpc(String jsonrpc) {
        return hasProperty("jsonrpc", equalTo(jsonrpc));
    }

    public static Matcher<ResponseJson> hasId(Serializable id) {
        return hasProperty("id", equalTo(id));
    }

    public static Matcher<ResponseJson> hasError(ErrorJson error) {
        return hasProperty("error", ErrorJsonThat.hasSameState(error));
    }

    public static Matcher<ResponseJson> hasResult(Value result) {
        return hasProperty("result", isSameValue(result));
    }
}
