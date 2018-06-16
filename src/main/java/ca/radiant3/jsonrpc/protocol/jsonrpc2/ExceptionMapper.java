package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.protocol.serialization.ErrorJson;

public interface ExceptionMapper {

    ErrorJson toError(Throwable thrown);
}
