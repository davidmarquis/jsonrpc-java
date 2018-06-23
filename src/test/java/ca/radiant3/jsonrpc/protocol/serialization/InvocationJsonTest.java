package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Value;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static ca.radiant3.jsonrpc.testkit.ValueThat.readsAs;
import static org.hamcrest.Matchers.*;
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
            Invocation invocation = json.addParameter(new ParameterJson(Value.of("Unnamed parameter")))
                                        .toInvocation();
            
            assertThat(invocation.getParameters(), contains(
                    hasProperty("value", readsAs(String.class, equalTo("Unnamed parameter")))
            ));
        }
    }
}