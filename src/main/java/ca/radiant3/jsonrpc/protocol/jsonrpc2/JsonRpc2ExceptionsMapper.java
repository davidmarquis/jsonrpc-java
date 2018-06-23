package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.protocol.serialization.ErrorJson;

public class JsonRpc2ExceptionsMapper implements ExceptionMapper {
    @Override
    public ErrorJson toError(Throwable thrown) {
        if (thrown instanceof NoSuchMethodException) {
            return Errors.methodNotFound();
        } else if (thrown instanceof InvalidProtocolVersion) {
            return Errors.invalidProtocol();
        } else if (thrown instanceof IllegalArgumentException) {
            return Errors.invalidParameters();
        }
        return ErrorJson.of(123).withMessage("uh oh!");
    }
}
