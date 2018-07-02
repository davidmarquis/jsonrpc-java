package ca.radiant3.jsonrpc;

import javax.lang.model.type.NullType;
import java.lang.reflect.Type;

public interface Value {
    Type type();
    Object readAs(Type type);

    default Object get() {
        return readAs(type());
    }

    static Value empty() {
        return of(null);
    }

    static Value of(Object value) {
        return of(value, value == null ? NullType.class : value.getClass());
    }

    static Value of(Object value, Type type) {
        return new Value() {
            @Override
            public Type type() {
                return type;
            }

            @Override
            public Object readAs(Type type) {
                return value;
            }
        };
    }
}
