package ca.radiant3.jsonrpc;

import java.lang.reflect.Type;
import java.util.Optional;

public class Result {
    private final Object value;
    private final Type hint;

    private Result(Object value, Type hint) {
        this.value = value;
        this.hint = hint;
    }

    public static Result of(Object result) {
        return new Result(result, Optional.ofNullable(result)
                                          .map(Object::getClass)
                                          .orElse(null));
    }

    public static Result of(Object result, Type hint) {
        return new Result(result, hint);
    }

    public Object getValue() {
        return value;
    }

    public Type getHint() {
        return hint;
    }
}
