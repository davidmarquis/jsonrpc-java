package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.Result;
import org.junit.Test;

public class JsonRpc2ProtocolTest {

    StubInvocationHandler handler = new StubInvocationHandler();
    JsonRpc2Protocol protocol = new JsonRpc2Protocol(handler, new JsonRpc2ExceptionsMapper());

    @Test
    public void invalidMethodName() {
        handler.responds(Result.of(""));
    }
}