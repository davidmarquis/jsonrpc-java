package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.protocol.Errors;
import ca.radiant3.jsonrpc.protocol.ExceptionMapper;
import ca.radiant3.jsonrpc.protocol.InvalidProtocolVersion;
import ca.radiant3.jsonrpc.protocol.payload.ErrorJson;

public class JsonRpc2ExceptionsMapper implements ExceptionMapper {
    @Override
    public ErrorJson toError(Throwable thrown) {
        if (thrown instanceof NoSuchMethodException) {
            return Errors.methodNotFound();
        } else if (thrown instanceof InvalidProtocolVersion) {
            return Errors.invalidProtocol(thrown.getMessage());
        } else if (thrown instanceof IllegalArgumentException) {
            return Errors.invalidParameters();
        }
        return ErrorJson.of(123).withMessage("uh oh!");
    }
}
