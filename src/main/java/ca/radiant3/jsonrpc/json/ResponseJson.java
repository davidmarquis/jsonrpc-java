package ca.radiant3.jsonrpc.json;

import ca.radiant3.jsonrpc.Value;

import java.io.Serializable;
import java.util.Optional;

public class ResponseJson {
    public static final String V2 = "2.0";

    private String jsonrpc = "2.0";
    private Serializable id;
    private Value result;
    private ErrorJson error;

    private ResponseJson(String version) {
        this.jsonrpc = version;
    }

    public static ResponseJson version(String version) {
        return new ResponseJson(version);
    }

    public static ResponseJson toNotification() {
        return to((String) null);
    }

    public static ResponseJson unboundError(ErrorJson details) {
        return toNotification().error(details);
    }

    public static ResponseJson to(Serializable id) {
        return ResponseJson.version(V2).withId(id);
    }

    public static ResponseJson to(InvocationJson invocation) {
        return to((String) null);
    }

    public ResponseJson withId(Serializable id) {
        this.id = id;
        return this;
    }

    public ResponseJson error(ErrorJson details) {
        this.error = details;
        return this;
    }

    public ResponseJson success(Value result) {
        this.result = result;
        return this;
    }

    public Value getResult() {
        return result;
    }

    public ErrorJson getError() {
        return error;
    }

    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> type) {
        return (T) Optional.ofNullable(result).map(value -> value.readAs(type)).orElse(null);
    }

    public Serializable getId() {
        return id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }
}
