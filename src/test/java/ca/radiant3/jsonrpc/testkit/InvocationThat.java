package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Args;
import ca.radiant3.jsonrpc.Invocation;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.AllOf.allOf;

public class InvocationThat {
    public static Matcher<Invocation> hasSameState(Invocation other) {
        return allOf(
                hasMethodName(other.getMethodName()),
                hasArguments(other.getArguments())
        );
    }

    public static Matcher<Invocation> hasMethodName(String methodName) {
        return hasProperty("methodName", equalTo(methodName));
    }

    public static Matcher<Invocation> hasArguments(Args arguments) {
        return hasProperty("arguments", ArgumentsThat.hasSameState(arguments));
    }
}
