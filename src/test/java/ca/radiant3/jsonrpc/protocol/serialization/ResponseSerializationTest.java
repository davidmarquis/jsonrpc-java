package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Result;
import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

@RunWith(Parameterized.class)
public class ResponseSerializationTest {

    @Parameterized.Parameters
    public static Collection<Object> implementations() {
        return List.of(
                new GsonPayloadSerializer()
        );
    }

    @Parameterized.Parameter
    public PayloadSerializer serializer;

    @Test
    public void errorResponse() throws IOException {
        String json = serialize(ResponseJson.unboundError(ErrorJson.of(1999).withMessage("An error occurred")));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"error\":{\"code\":1999,\"message\":\"An error occurred\"}}"));
    }

    @Test
    public void successResponseWithoutId() throws IOException {
        String json = serialize(ResponseJson.toNotification().success(Result.of("Hello World!")));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"result\":\"Hello World!\"}"));
    }

    @Test
    public void successResponseAsString() throws IOException {
        String json = serialize(ResponseJson.toInvocation("991").success(Result.of("Hello World!")));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"id\":\"991\",\"result\":\"Hello World!\"}"));
    }

    @Test
    public void successResponseAsNumber() throws IOException {
        String json = serialize(ResponseJson.toInvocation("991").success(Result.of(90.12d)));

        assertThat(json, sameJSONAs("{\"jsonrpc\":\"2.0\",\"id\":\"991\",\"result\":90.12}"));
    }

    @Test
    public void successResponseAsObject() throws IOException {
        String json = serialize(ResponseJson.toInvocation("991").success(
                                                     Result.of(new DummyObject("some string", 999))));

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