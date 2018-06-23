package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.protocol.serialization.ErrorJson;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonRpc2ExceptionsMapperTest {

    JsonRpc2ExceptionsMapper mapper = new JsonRpc2ExceptionsMapper();

    @Test
    public void methodNotFound() {
        ErrorJson error = mapper.toError(new NoSuchMethodException("methodName"));

        assertThat(error.getCode(), is(-32601));
        assertThat(error.getMessage(), is("Method not found"));
    }

    @Test
    public void invalidProtocol() {
        ErrorJson error = mapper.toError(new InvalidProtocolVersion("1.0"));

        assertThat(error.getCode(), is(-32600));
        assertThat(error.getMessage(), is("Invalid protocol"));
    }

    @Test
    public void invalidParams() {
        ErrorJson error = mapper.toError(new IllegalArgumentException());

        assertThat(error.getCode(), is(-32602 ));
        assertThat(error.getMessage(), is("Invalid params"));
    }
}