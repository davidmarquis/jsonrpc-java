package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Arg;
import ca.radiant3.jsonrpc.Args;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.emptyIterableOf;

public class ParametersThat {

    public static Matcher<Args> hasSameState(Args other) {
        return hasParams(other.list());
    }

    private static Matcher<Args> hasParams(List<Arg> args) {
        Matcher<? super List<Arg>> paramsMatcher = emptyIterableOf(Arg.class);
        if (!args.isEmpty()) {
            paramsMatcher = IsIterableContainingInOrder.contains(
                    args.stream().map(ParamThat::hasSameState).collect(toList())
            );
        }

        return new FeatureMatcher<Args, List<Arg>>(paramsMatcher,
                                                   "with parameters", "parameters") {
            @Override
            protected List<Arg> featureValueOf(Args actual) {
                return actual.list();
            }
        };
    }
}
