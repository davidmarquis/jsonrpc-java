package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.Example;
import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.json.ResponseJson;
import ca.radiant3.jsonrpc.protocol.Errors;
import ca.radiant3.jsonrpc.protocol.InvocationPayload;
import ca.radiant3.jsonrpc.protocol.ResponsePayload;
import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static ca.radiant3.jsonrpc.testkit.ErrorJsonThat.hasSameState;
import static ca.radiant3.jsonrpc.testkit.ValueThat.hasValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class JsonRpc2ProtocolTest {

    static StubInvocationHandler handler = new StubInvocationHandler();
    static JsonRpc2Protocol protocol = new JsonRpc2Protocol(handler,
                                                            new GsonPayloadSerializer(),
                                                            new JsonRpc2ExceptionsMapper());

    public static class ErrorHandling {
        @Test
        public void invalidMimeType() {
            ResponseJson response = invoke(InvocationPayload.from(null, "application/other"));


            assertThat(response.getError(), hasSameState(Errors.invalidMimeType("application/other")));
            assertThat(response.getResult(), nullValue());
        }

        @Test
        public void invalidFormat() {
            ResponseJson response = invoke(Example.get("/examples/invalid/notification-badly-formed.json"));

            assertThat(response.getError(), hasSameState(Errors.invalidFormat()));
            assertThat(response.getResult(), nullValue());
        }

        @Test
        public void invalidProtocol() {
            ResponseJson response = invoke(Example.get("/examples/invalid/notification-invalid-protocol.json"));

            assertThat(response.getError(), hasSameState(Errors.invalidProtocol("1.0")));
            assertThat(response.getResult(), nullValue());
        }

        @Test
        public void exceptionDuringInvocationIsTranslatedToErrorResponse() {
            handler.throwing(new NoSuchMethodException());

            ResponseJson response = invoke(Example.get("/examples/notification-no-param.json"));

            assertThat(response.getError(), hasSameState(Errors.methodNotFound()));
            assertThat(response.getResult(), nullValue());
        }
    }

    public static class SuccessfulInvocation {
        Value result = Value.of("some result");
        JsonRpc2Protocol.JsonResponsePayload response;

        @Before
        public void setUp() {
            handler.returns(result);
            response = (JsonRpc2Protocol.JsonResponsePayload) protocol.invoke(
                    Example.get("/examples/notification-no-param.json").asInvocation());
        }

        @Test
        public void includesMethodReturnValue() {
            assertThat(response.getResponse().getResult(), hasValue(result));
            assertThat(response.getResponse().getError(), nullValue());
        }

        @Test
        public void respondsWithJsonMimeType() {
            assertThat(response.mimeType(), equalTo("application/json"));
        }

        @Test
        public void respondsValidJson() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.writeTo(out);
            JsonElement parsed = new JsonParser().parse(new StringReader(out.toString()));
            assertThat(parsed, notNullValue());
        }
    }

    private static ResponseJson invoke(Example example) {
        return invoke(example.asInvocation());
    }

    private static ResponseJson invoke(InvocationPayload invocation) {
        ResponsePayload response = protocol.invoke(invocation);
        return ((JsonRpc2Protocol.JsonResponsePayload) response).getResponse();
    }
}