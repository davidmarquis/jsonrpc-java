package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.protocol.payload.ParametersJson;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class ParametersJsonThat {

    public static Matcher<ParametersJson> hasSameState(ParametersJson other) {
        if (other.hasNamedParameters()) {
            return hasNamedParameters(other.toMap());
        } else {
            return hasUnnamedParameters(other.values());
        }
    }

    private static Matcher<ParametersJson> hasUnnamedParameters(List<Value> params) {
        return new FeatureMatcher<>(equalTo(params), "unnamed parameters", "with unnamed parameters") {
            @Override
            protected List<Value> featureValueOf(ParametersJson actual) {
                return actual.values();
            }
        };
    }

    public static Matcher<ParametersJson> hasNamedParameters(Map<String, Value> params) {
        return new FeatureMatcher<>(equalTo(params), "named parameters", "with named parameters") {
            @Override
            protected Map<String, Value> featureValueOf(ParametersJson actual) {
                return actual.toMap();
            }
        };
    }
}
