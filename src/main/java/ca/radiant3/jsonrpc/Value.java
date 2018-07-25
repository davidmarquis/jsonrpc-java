package ca.radiant3.jsonrpc;

import javax.lang.model.type.NullType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface Value {
    Optional<Type> getType();
    Object readAs(Type type) throws ClassCastException;

    static Value empty() {
        return of(null);
    }

    static Value of(Object value) {
        return of(value, value == null ? NullType.class : value.getClass());
    }

    static Value of(int value) {
        return of(value, int.class);
    }

    static Value of(Object value, Type type) {
        return new TypedValue(value, type);
    }

    default boolean isCompatibleWith(Type type) {
        try {
            readAs(type);
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    class TypedValue implements Value {
        private static final Map<Class, Class> primitivesToBoxed = Map.of(
                char.class, Character.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class
        );

        private final Object value;
        private final Type type;

        private TypedValue(Object value, Type type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public Optional<Type> getType() {
            return Optional.of(type);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object readAs(Type type) {
            if (value == null) {
                return null;
            }

            if (type instanceof Class) {
                if (((Class) type).isPrimitive()) {
                    if (value.getClass().equals(primitivesToBoxed.get(type))) {
                        return value;
                    } else {
                        throw new ClassCastException("Cannot cast " + value + " to " + type);
                    }
                }

                return ((Class<?>) type).cast(value);
            }

            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TypedValue that = (TypedValue) o;
            return Objects.equals(value, that.value) &&
                    Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, type);
        }

        @Override
        public String toString() {
            return Objects.toString(value) + '<' + type.getTypeName() + '>';
        }
    }
}
