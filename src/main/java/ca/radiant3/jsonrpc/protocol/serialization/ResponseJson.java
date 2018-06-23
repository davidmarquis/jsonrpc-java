package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Result;

import java.io.Serializable;

public class ResponseJson {
    private final String jsonrpc = "2.0";
    private final Serializable id;
    private Result result;
    private ErrorJson error;

    private ResponseJson(String id) {
        this.id = id;
    }

    public static ResponseJson toInvocation(String id) {
        return new ResponseJson(id);
    }

    public static ResponseJson toNotification() {
        return new ResponseJson(null);
    }

    public static ResponseJson unboundError(ErrorJson details) {
        return toNotification().error(details);
    }

    public static ResponseJson to(InvocationJson invocation) {
        return toInvocation(null);
    }

    public ResponseJson error(ErrorJson details) {
        this.error = details;
        return this;
    }

    public ResponseJson success(Result result) {
        this.result = result;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public ErrorJson getError() {
        return error;
    }
}
