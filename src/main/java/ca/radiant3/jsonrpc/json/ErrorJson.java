package ca.radiant3.jsonrpc.json;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class ErrorJson {
    private final int code;
    private final Map<String, String> data = new LinkedHashMap<>();

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

    public ErrorJson withData(String key, String value) {
        data.put(key, value);
        return this;
    }

    public Map<String, String> getData() {
        return unmodifiableMap(data);
    }
}
