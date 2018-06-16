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

        assertThat(error.code(), is(-32601));
        assertThat(error.message(), is("Method not found"));
    }

    @Test
    public void invalidParams() {
        ErrorJson error = mapper.toError(new IllegalArgumentException());

        assertThat(error.code(), is(-32602 ));
        assertThat(error.message(), is("Invalid params"));
    }
}