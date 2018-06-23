package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Result;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

public class ResultThat {

    public static Matcher<Result> hasSameState(Result other) {
        return allOf(
                hasValue(equalTo(other.getValue())),
                hasHint(equalTo(other.getHint()))
        );
    }

    public static Matcher<Result> hasValue(Matcher<Object> matching) {
        return hasProperty("value", matching);
    }

    public static Matcher<Result> hasHint(Matcher<Object> matching) {
        return hasProperty("hint", matching);
    }
}
