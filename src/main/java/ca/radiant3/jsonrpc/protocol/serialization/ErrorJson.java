package ca.radiant3.jsonrpc.protocol.serialization;

public class ErrorJson {
    private int code;
    private String message;

    private ErrorJson(int code) {
        this.code = code;
    }

    public static ErrorJson of(int code) {
        return new ErrorJson(code);
    }

    public ErrorJson withMessage(String message) {
        this.message = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
