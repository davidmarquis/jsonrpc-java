package ca.radiant3.jsonrpc.json;

import ca.radiant3.jsonrpc.Arg;
import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Value;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static ca.radiant3.jsonrpc.json.ParametersJson.named;
import static ca.radiant3.jsonrpc.json.ParametersJson.unnamed;
import static ca.radiant3.jsonrpc.testkit.InvocationJsonThat.*;
import static ca.radiant3.jsonrpc.testkit.ParamThat.hasSameState;
import static ca.radiant3.jsonrpc.testkit.ParametersJsonThat.hasSameState;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class InvocationJsonTest {

    public static class BuildsJson {

        InvocationJson json = new InvocationJson();

        @Test
        public void withMethodName() {
            Invocation invocation = json.withMethod("someMethod")
                                        .toInvocation();
            assertThat(invocation.getMethodName(), equalTo("someMethod"));
        }

        @Test
        public void withParameter() {
            Invocation invocation = json.withParameters(unnamed(Value.of("Unnamed parameter"))).toInvocation();

            assertThat(invocation.getArguments().list(), contains(
                    hasSameState(Arg.of(Value.of("Unnamed parameter")))
            ));
        }

        @Test
        public void withNamedParameter() {
            Invocation invocation = json.withParameters(named().add("param1", Value.of("Named parameter")))
                                        .toInvocation();

            assertThat(invocation.getArguments().list(), contains(
                    hasSameState(Arg.of(Value.of("Named parameter")).named("param1"))
            ));
        }

    }

    public static class CreatesJsonFromInvocation {
        Invocation invocation = Invocation.of("someMethod");

        @Test
        public void includesJsonRpcVersion() {
            InvocationJson json = InvocationJson.of(invocation);
            assertThat(json, hasJsonrpc("2.0"));
        }

        @Test
        public void includesMethodName() {
            InvocationJson json = InvocationJson.of(invocation);
            assertThat(json, hasMethod("someMethod"));
        }

        @Test
        public void includesParameters() {
            InvocationJson json = InvocationJson.of(invocation
                                                            .withArgument(Arg.of(Value.of("someValue"))));

            assertThat(json, hasParameters(hasSameState(unnamed(Value.of("someValue")))));
        }
    }
}