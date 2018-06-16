package ca.radiant3.jsonrpc;

import java.lang.reflect.Type;

public interface Value {
    Object readAs(Type type);

    static Value of(Object value) {
        return (type) -> value;
    }
}
