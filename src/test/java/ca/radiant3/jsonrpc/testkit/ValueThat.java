package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Value;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class ValueThat {

    public static <T> Matcher<Value> readsAs(Class<T> type, Matcher<T> andMatches) {
        return new FeatureMatcher<>(andMatches, "reads as " + type, "as " + type) {
            @Override
            @SuppressWarnings("unchecked")
            protected T featureValueOf(Value actual) {
                return (T) actual.readAs(type);
            }
        };
    }
}
