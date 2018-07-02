package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Param;
import ca.radiant3.jsonrpc.Value;
import org.hamcrest.Matcher;

import static ca.radiant3.jsonrpc.testkit.ValueThat.isSameValue;
import static ca.radiant3.jsonrpc.testkit.ValueThat.readsAs;
import static org.hamcrest.Matchers.*;

public class ParamThat {
    public static Matcher<Param> hasSameState(Param other) {
        return allOf(
                withValue(isSameValue(other.getValue())),
                withName(other.getName())
        );
    }

    public static Matcher<Param> withValue(Matcher<? super Value> matching) {
        return hasProperty("value", matching);
    }

    public static <T> Matcher<Param> withValue(Class<T> type, Matcher<T> matching) {
        return hasProperty("value", readsAs(type, matching));
    }

    public static Matcher<Param> withName(String name) {
        return hasProperty("name", equalTo(name));
    }
}
