package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Example;
import ca.radiant3.jsonrpc.Value;
import org.junit.Test;

import java.util.Objects;

import static ca.radiant3.jsonrpc.testkit.ResponseJsonThat.hasSameState;
import static org.junit.Assert.assertThat;

public class ResponseDeserializationTest extends BaseSerializationTest {

    @Test
    public void successWithString() {
        ResponseJson response = serializer.readResponse(
                Example.get("/examples/response/success-string.json").read());

        assertThat(response, hasSameState(aResponse().withId(1992)
                                                     .success(Value.of("ok"))));
    }

    @Test
    public void successWithNumber() {
        ResponseJson response = serializer.readResponse(
                Example.get("/examples/response/success-number.json").read());

        assertThat(response, hasSameState(aResponse().withId(1992)
                                                     .success(Value.of(123))));
    }

    @Test
    public void successWithObject() {
        ResponseJson response = serializer.readResponse(
                Example.get("/examples/response/success-object.json").read());

        assertThat(response, hasSameState(aResponse().withId(1992)
                                                     .success(Value.of(new ExampleObject("value")))));
    }

    @Test
    public void notificationError() {
        ResponseJson response = serializer.readResponse(
                Example.get("/examples/response/notification-error.json").read());

        assertThat(response, hasSameState(aResponse()
                                                  .error(ErrorJson.of(-19300).withMessage("This is a message"))));
    }

    @Test
    public void error() {
        ResponseJson response = serializer.readResponse(
                Example.get("/examples/response/error.json").read());

        assertThat(response, hasSameState(aResponse().withId(1992)
                                                     .error(ErrorJson.of(-19300).withMessage("This is a message"))));
    }

    private ResponseJson aResponse() {
        return ResponseJson.version("2.0");
    }

    public static class ExampleObject {
        private String property;

        public ExampleObject(String property) {
            this.property = property;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExampleObject that = (ExampleObject) o;
            return Objects.equals(property, that.property);
        }

        @Override
        public int hashCode() {
            return Objects.hash(property);
        }
    }
}