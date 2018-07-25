package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.json.InvocationJson;
import ca.radiant3.jsonrpc.testkit.ExamplePayload;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class InvocationDeserializationTest extends BaseSerializationTest {

    @Test
    public void notificationWithoutParameters() {
        InvocationJson invocation = serializer.readInvocation(
                ExamplePayload.get("/examples/notification-no-param.json").read());

        assertThat(invocation.getJsonrpc(), is("2.0"));
        assertThat(invocation.getMethod(), is("someMethod"));
    }

    @Test
    public void notificationWithParametersArray() {
        InvocationJson invocation = serializer.readInvocation(
                ExamplePayload.get("/examples/notification-params-array.json").read());

        List<Value> params = invocation.getParams().values();

        assertThat(params, hasSize(3));

        assertThat(params.get(0).readAs(Integer.class), equalTo(1));
        assertThat(params.get(1).readAs(String.class), is("2"));
        assertThat(params.get(2).readAs(DummyParam.class), is(new DummyParam("value")));
    }

    @Test
    public void notificationWithParametersNamed() {
        InvocationJson invocation = serializer.readInvocation(
                ExamplePayload.get("/examples/notification-params-named.json").read());

        Map<String, Value> params = invocation.getParams().toMap();

        assertThat(params.size(), is(3));

        assertThat(params.get("param1").readAs(Integer.class), equalTo(1));
        assertThat(params.get("param2").readAs(String.class), is("2"));
        assertThat(params.get("param3").readAs(DummyParam.class), is(new DummyParam("value")));
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