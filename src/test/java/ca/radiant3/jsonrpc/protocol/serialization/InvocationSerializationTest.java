package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.json.InvocationJson;
import ca.radiant3.jsonrpc.json.ParametersJson;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static ca.radiant3.jsonrpc.json.ParametersJson.named;
import static org.junit.Assert.assertThat;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class InvocationSerializationTest extends BaseSerializationTest {
    @Test
    public void notificationWithoutParameter() throws IOException {
        String json = serialize(new InvocationJson()
                                        .withJsonrpc("2.0")
                                        .withMethod("someMethod"));

        assertThat(json, sameJSONAs("{\"jsonrpc\": \"2.0\", \"method\": \"someMethod\"}"));
    }

    @Test
    public void notificationWithUnnamedStringParameter() throws IOException {
        String json = serialize(new InvocationJson()
                                        .withJsonrpc("2.0")
                                        .withMethod("someMethod")
                                        .withParameters(ParametersJson.of(
                                                Value.of("toto")
                                        )));

        assertThat(json, sameJSONAs("{\"jsonrpc\": \"2.0\", \"method\": \"someMethod\", \"params\": [\"toto\"]}"));
    }

    @Test
    public void notificationWithNamedStringParameter() throws IOException {
        String json = serialize(new InvocationJson()
                                        .withJsonrpc("2.0")
                                        .withMethod("someMethod")
                                        .withParameters(named().add("param1", Value.of("toto"))));

        assertThat(json, sameJSONAs("{\"jsonrpc\": \"2.0\", \"method\": \"someMethod\", \"params\": {\"param1\": \"toto\"}}"));
    }

    @Test
    public void notificationWithNamedNullParameter() throws IOException {
        String json = serialize(new InvocationJson()
                                        .withJsonrpc("2.0")
                                        .withMethod("someMethod")
                                        .withParameters(named().add("param1", Value.empty())));

        assertThat(json, sameJSONAs("{\"jsonrpc\": \"2.0\", \"method\": \"someMethod\", \"params\": {\"param1\": null}}"));
    }

    @Test
    public void notificationWithMultipleUnnamedParameters() throws IOException {
        String json = serialize(new InvocationJson()
                                        .withJsonrpc("2.0")
                                        .withMethod("someMethod")
                                        .withParameters(ParametersJson.of(
                                                Value.of("toto"),
                                                Value.empty(),
                                                Value.of(120)
                                        )));

        assertThat(json, sameJSONAs("{\"jsonrpc\": \"2.0\", \"method\": \"someMethod\", \"params\": [\"toto\",null,120]}"));
    }

    @Test
    public void notificationWithMultipleNamedParameters() throws IOException {
        String json = serialize(new InvocationJson()
                                        .withJsonrpc("2.0")
                                        .withMethod("someMethod")
                                        .withParameters(named()
                                                                .add("param1", Value.of("toto"))
                                                                .add("param2", Value.empty())
                                                                .add("param3", Value.of(120))
                                        ));

        assertThat(json, sameJSONAs("{\"jsonrpc\": \"2.0\", \"method\": \"someMethod\", \"params\": {\"param1\":\"toto\",\"param2\":null,\"param3\":120}}"));
    }

    private String serialize(InvocationJson invocation) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializer.serialize(invocation, out);

        return out.toString();
    }
}
