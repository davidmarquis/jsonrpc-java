package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Result;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class ResultThat {

    public static Matcher<Result> hasValue(Matcher<Object> matching) {
        return new FeatureMatcher<>(matching, "with value", "value") {
            @Override
            protected Object featureValueOf(Result actual) {
                return actual.value();
            }
        };
    }
}
