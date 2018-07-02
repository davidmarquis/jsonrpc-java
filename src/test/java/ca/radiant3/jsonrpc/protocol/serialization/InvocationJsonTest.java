package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Param;
import ca.radiant3.jsonrpc.Value;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static ca.radiant3.jsonrpc.protocol.serialization.ParametersJson.named;
import static ca.radiant3.jsonrpc.testkit.ParamThat.hasSameState;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class InvocationJsonTest {

    public static class CreatesInvocation {

        InvocationJson json = new InvocationJson();

        @Test
        public void withMethodName() {
            Invocation invocation = json.withMethod("someMethod")
                                        .toInvocation();
            assertThat(invocation.getMethodName(), equalTo("someMethod"));
        }

        @Test
        public void withParameter() {
            Invocation invocation = json.withParameters(ParametersJson.of(Value.of("Unnamed parameter"))).toInvocation();
            
            assertThat(invocation.getParameters().list(), contains(
                    hasSameState(new Param(Value.of("Unnamed parameter")))
            ));
        }

        @Test
        public void withNamedParameter() {
            Invocation invocation = json.withParameters(named()
                                                                .add("param1", Value.of("Named parameter")))
                                        .toInvocation();

            assertThat(invocation.getParameters().list(), contains(
                    hasSameState(new Param(Value.of("Named parameter")).named("param1"))
            ));
        }

    }
}