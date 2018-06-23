package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.Result;
import ca.radiant3.jsonrpc.protocol.InvocationPayload;
import ca.radiant3.jsonrpc.protocol.ResponsePayload;
import ca.radiant3.jsonrpc.protocol.serialization.ErrorJson;
import ca.radiant3.jsonrpc.protocol.serialization.ResponseJson;
import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static ca.radiant3.jsonrpc.testkit.ResultThat.hasSameState;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasProperty;
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


            assertThat(response.getError(), sameCodeAs(Errors.invalidMimeType()));
            assertThat(response.getResult(), nullValue());
        }

        @Test
        public void invalidFormat() {
            ResponseJson response = invokeFrom("/examples/invalid/notification-badly-formed.json");

            assertThat(response.getError(), sameCodeAs(Errors.invalidFormat()));
            assertThat(response.getResult(), nullValue());
        }

        @Test
        public void invalidProtocol() {
            ResponseJson response = invokeFrom("/examples/invalid/notification-invalid-protocol.json");

            assertThat(response.getError(), sameCodeAs(Errors.invalidProtocol()));
            assertThat(response.getResult(), nullValue());
        }

        @Test
        public void exceptionDuringInvocationIsTranslatedToErrorResponse() {
            handler.throwing(new NoSuchMethodException());

            ResponseJson response = invokeFrom("/examples/notification-no-param.json");

            assertThat(response.getError(), sameCodeAs(Errors.methodNotFound()));
            assertThat(response.getResult(), nullValue());
        }
    }

    public static class SuccessfulInvocation {
        Result result = Result.of("some result");
        JsonRpc2Protocol.JsonResponsePayload response;

        @Before
        public void setUp() {
            handler.responds(result);
            response = (JsonRpc2Protocol.JsonResponsePayload) protocol.invoke(example("/examples/notification-no-param.json"));
        }

        @Test
        public void includesMethodReturnValue() {
            assertThat(response.getResponse().getResult(), hasSameState(result));
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

    private static ResponseJson invokeFrom(String example) {
        return invoke(example(example));
    }

    private static InvocationPayload example(String example) {
        return InvocationPayload.from(
                JsonRpc2ProtocolTest.class.getResourceAsStream(example),
                "application/json");
    }

    private static ResponseJson invoke(InvocationPayload invocation) {
        ResponsePayload response = protocol.invoke(invocation);
        return ((JsonRpc2Protocol.JsonResponsePayload) response).getResponse();
    }

    private static Matcher<ErrorJson> sameCodeAs(ErrorJson error) {
        return hasProperty("code", equalTo(error.getCode()));
    }

}