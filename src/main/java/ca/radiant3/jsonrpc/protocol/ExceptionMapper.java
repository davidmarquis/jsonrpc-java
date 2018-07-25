package ca.radiant3.jsonrpc.protocol;

import ca.radiant3.jsonrpc.protocol.payload.ErrorJson;

public interface ExceptionMapper {

    ErrorJson toError(Throwable thrown);
}
