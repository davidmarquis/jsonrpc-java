package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Value;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.lang.reflect.Type;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class ValueThat {
    public static <T> Matcher<Value> readsAs(Type type, Matcher<T> andMatches) {
        return new FeatureMatcher<>(andMatches, "reads as " + type, "read as " + type) {
            @Override
            @SuppressWarnings("unchecked")
            protected T featureValueOf(Value actual) {
                return (T) actual.readAs(type);
            }
        };
    }

    public static Matcher<Value> readsAsString(String value) {
        return readsAs(String.class, equalTo(value));
    }

    public static Matcher<? super Value> hasValue(Value result) {
        if (result == null) return nullValue();
        assert result.getType().isPresent();

        Type type = result.getType().get();
        return ValueThat.readsAs(type, equalTo(result.readAs(type)));
    }
}
