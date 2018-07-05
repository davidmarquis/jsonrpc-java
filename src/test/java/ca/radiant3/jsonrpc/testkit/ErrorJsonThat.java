package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.json.ErrorJson;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

public class ErrorJsonThat {
    public static Matcher<? super ErrorJson> hasSameState(ErrorJson error) {
        return error == null ? nullValue() : allOf(
            hasProperty("code", equalTo(error.getCode())),
            hasProperty("message", equalTo(error.getMessage())),
            hasProperty("data", equalTo(error.getData()))
        );
    }
}
