package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;
import ca.radiant3.jsonrpc.testkit.OptionalMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ca.radiant3.jsonrpc.testkit.OptionalMatchers.isPresentAnd;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class DeserializationTest {

    @Parameterized.Parameters
    public static Collection<Object> implementations() {
        return List.of(
                new GsonPayloadSerializer()
        );
    }

    @Parameterized.Parameter
    public PayloadSerializer serializer;

    @Test
    public void notificationWithoutParameters() {
        InvocationJson invocation = serializer.readInvocation(
                example("/examples/notification-no-param.json"));

        assertThat(invocation.getJsonRpc(), is("2.0"));
        assertThat(invocation.getMethod(), is("someMethod"));
    }

    @Test
    public void notificationWithParametersArray() {
        InvocationJson invocation = serializer.readInvocation(
                example("/examples/notification-params-array.json"));

        assertThat(invocation.getParams(), hasSize(3));
        assertThat(invocation.getParams(), everyItem(hasProperty("name", OptionalMatchers.empty())));
                                                                               
        assertThat(invocation.getParams().get(0).getValue().readAs(Integer.class), equalTo(1));
        assertThat(invocation.getParams().get(1).getValue().readAs(String.class), is("2"));
        assertThat(invocation.getParams().get(2).getValue().readAs(DummyParam.class), is(new DummyParam("value")));
    }

    @Test
    public void notificationWithParametersNamed() {
        InvocationJson invocation = serializer.readInvocation(
                example("/examples/notification-params-named.json"));

        assertThat(invocation.getParams(), hasSize(3));

        assertThat(invocation.getParams().get(0).getName(), isPresentAnd(equalTo("param1")));
        assertThat(invocation.getParams().get(0).getValue().readAs(Integer.class), equalTo(1));
        assertThat(invocation.getParams().get(1).getName(), isPresentAnd(equalTo("param2")));
        assertThat(invocation.getParams().get(1).getValue().readAs(String.class), is("2"));
        assertThat(invocation.getParams().get(2).getName(), isPresentAnd(equalTo("param3"))   );
        assertThat(invocation.getParams().get(2).getValue().readAs(DummyParam.class), is(new DummyParam("value")));
    }

    private InputStream example(String example) {
        return getClass().getResourceAsStream(example);
    }

    public static class DummyParam {
        private String attribute;

        public DummyParam(String attribute) {
            this.attribute = attribute;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DummyParam that = (DummyParam) o;
            return Objects.equals(attribute, that.attribute);
        }

        @Override
        public int hashCode() {

            return Objects.hash(attribute);
        }
    }
}