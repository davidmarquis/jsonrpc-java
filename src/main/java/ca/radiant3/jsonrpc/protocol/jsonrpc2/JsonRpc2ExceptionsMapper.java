package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.protocol.serialization.ErrorJson;

public class JsonRpc2ExceptionsMapper implements ExceptionMapper {
    @Override
    public ErrorJson toError(Throwable thrown) {
        ErrorJson error = new ErrorJson();
        if (thrown instanceof NoSuchMethodException) {
            error.withCode(-32601).withMessage("Method not found");
        } else if (thrown instanceof IllegalArgumentException) {
            error.withCode(-32602).withMessage("Invalid params");
        } else {
            error.withCode(123).withMessage("uh oh!");
        }
        return error;
    }
}
