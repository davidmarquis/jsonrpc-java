package ca.radiant3.jsonrpc.protocol.serialization;

public class ErrorJson {
    private int code;
    private String message;

    public ErrorJson withCode(int code) {
        this.code = code;
        return this;
    }

    public ErrorJson withMessage(String message) {
        this.message = message;
        return this;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
