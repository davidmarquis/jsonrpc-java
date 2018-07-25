package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Arg;
import ca.radiant3.jsonrpc.Value;
import org.hamcrest.Matcher;

import static ca.radiant3.jsonrpc.testkit.ValueThat.hasValue;
import static org.hamcrest.Matchers.*;

public class ParamThat {
    public static Matcher<Arg> hasSameState(Arg other) {
        return allOf(
                withValue(hasValue(other.getValue())),
                withName(other.getName())
        );
    }

    public static Matcher<Arg> withValue(Matcher<? super Value> matching) {
        return hasProperty("value", matching);
    }

    public static Matcher<Arg> withName(String name) {
        return hasProperty("name", equalTo(name));
    }
}
