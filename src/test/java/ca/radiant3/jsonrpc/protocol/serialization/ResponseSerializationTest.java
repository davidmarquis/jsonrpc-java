package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.protocol.payload.ErrorJson;
import ca.radiant3.jsonrpc.protocol.payload.ResponseJson;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class ResponseSerializationTest extends BaseSerializationTest {

    @Test
    public void errorResponse() throws IOException {
        String json = serialize(ResponseJson.unboundError(ErrorJson.of(1999).withMessage("An error occurred")));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"id\":null,\"error\":{\"code\":1999,\"message\":\"An error occurred\"}}"));
    }

    @Test
    public void successResponseWithoutId() throws IOException {
        String json = serialize(ResponseJson.toNotification().success(Value.of("Hello World!")));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"id\":null,\"result\":\"Hello World!\"}"));
    }

    @Test
    public void successResponseAsString() throws IOException {
        String json = serialize(ResponseJson.to("991").success(Value.of("Hello World!")));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"id\":\"991\",\"result\":\"Hello World!\"}"));
    }

    @Test
    public void successResponseAsNumber() throws IOException {
        String json = serialize(ResponseJson.to("991").success(Value.of(90.12d)));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"id\":\"991\",\"result\":90.12}"));
    }

    @Test
    public void successResponseAsObject() throws IOException {
        String json = serialize(ResponseJson.to("991").success(
                Value.of(new DummyObject("some string", 999))));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"id\":\"991\",\"result\":{\"string\": \"some string\", \"number\": 999}}"));
    }

    private String serialize(ResponseJson responseJson) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializer.serialize(responseJson, out);

        return out.toString();
    }

    private static class DummyObject {
        private String string;
        private int number;

        public DummyObject(String string, int number) {
            this.string = string;
            this.number = number;
        }
    }
}