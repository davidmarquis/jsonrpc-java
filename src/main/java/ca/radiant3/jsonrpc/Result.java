package ca.radiant3.jsonrpc;

import java.lang.reflect.Type;

public class Result {
    private final Object value;
    private final Type hint;

    private Result(Object value, Type hint) {
        this.value = value;
        this.hint = hint;
    }

    public static Result of(Object result) {
        return new Result(result, null);
    }

    public static Result of(Object result, Type hint) {
        return new Result(result, null);
    }

    public Object value() {
        return value;
    }

    public Type hint() {
        return hint;
    }
}
