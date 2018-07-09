package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.protocol.serialization.gson.JsonElementValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class JsonElementValueTest {
    private static Gson gson = new Gson();

    public static class OfCharacterSizedInteger {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive(5));

        @Test
        public void conversions() {
            readsAs(value, List.of(Character.class, char.class), is('5'));
            readsAs(value, List.of(Short.class, short.class), is((short) 5));
            readsAs(value, List.of(Integer.class, int.class), is(5));
            readsAs(value, List.of(Long.class, long.class), is((long) 5));
            readsAs(value, List.of(Float.class, float.class), is(5f));
            readsAs(value, List.of(Double.class, double.class), is(5d));
            readsAs(value, List.of(String.class), is("5"));
        }
    }

    public static class OfShortSizedInteger {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive(123));

        @Test
        public void conversions() {
            readsAs(value, List.of(Integer.class, int.class), is(123));
            readsAs(value, List.of(Long.class, long.class), is((long) 123));
            readsAs(value, List.of(Short.class, short.class), is((short) 123));
            readsAs(value, List.of(Float.class, float.class), is(123f));
            readsAs(value, List.of(Double.class, double.class), is(123d));
            readsAs(value, List.of(String.class), is("123"));

            doesNotReadAs(value, List.of(Character.class, char.class));
        }
    }

    public static class OfLongSizedInteger {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive(991882123));

        @Test
        public void conversions() {
            readsAs(value, List.of(Short.class, short.class), is((short) -5237));  // overflows
            readsAs(value, List.of(Integer.class, int.class), is(991882123));
            readsAs(value, List.of(Long.class, long.class), is((long) 991882123));
            readsAs(value, List.of(Float.class, float.class), is(991882123f));
            readsAs(value, List.of(Double.class, double.class), is(991882123d));
            readsAs(value, List.of(String.class), is("991882123"));

            doesNotReadAs(value, List.of(Character.class, char.class));
        }
    }

    @RunWith(Parameterized.class)
    public static class OfDecimal {
        private static final float FLOAT = 345.19f;
        private static final double DOUBLE = 345.19d;

        @Parameterized.Parameters
        public static Collection<JsonElementValue> subjects() {
            return Arrays.asList(
                    new JsonElementValue(gson, new JsonPrimitive(FLOAT)),
                    new JsonElementValue(gson, new JsonPrimitive(DOUBLE))
            );
        }

        @Parameterized.Parameter
        public JsonElementValue value;

        @Test
        public void conversions() {
            readsAs(value, List.of(Short.class, short.class), is((short) 345));
            readsAs(value, List.of(Integer.class, int.class), is(345));
            readsAs(value, List.of(Long.class, long.class), is((long) 345));
            readsAs(value, List.of(Float.class, float.class), is(345.19f));
            readsAs(value, List.of(Double.class, double.class), allOf(
                    greaterThanOrEqualTo(345.19d), lessThan(345.2d)
            ));
            readsAs(value, List.of(String.class), is("345.19"));

            doesNotReadAs(value, List.of(Character.class, char.class));
        }
    }

    public static class OfNonNumericString {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive("abc"));

        @Test
        public void conversions() {
            readsAs(value, List.of(String.class), is("abc"));

            doesNotReadAs(value, List.of(
                    Character.class, char.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class
            ));
        }
    }

    public static class OfNumericDecimalString {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive("199.19"));

        @Test
        public void conversions() {
            readsAs(value, List.of(Float.class, float.class), is(199.19f));
            readsAs(value, List.of(Double.class, double.class), allOf(
                    greaterThanOrEqualTo(199.19d), lessThan(199.2d)
            ));
            readsAs(value, List.of(String.class), is("199.19"));

            doesNotReadAs(value, List.of(
                    Character.class, char.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class
            ));
        }
    }

    public static class OfNumericIntegerString {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive("199"));

        @Test
        public void conversions() {
            readsAs(value, List.of(Short.class, short.class), is((short) 199));
            readsAs(value, List.of(Integer.class, int.class), is(199));
            readsAs(value, List.of(Long.class, long.class), is((long) 199));
            readsAs(value, List.of(Float.class, float.class), is(199f));
            readsAs(value, List.of(Double.class, double.class), is(199d));
            readsAs(value, List.of(String.class), is("199"));

            doesNotReadAs(value, List.of(Character.class, char.class));
        }
    }

    public static class OfSingleCharacterString {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive("a"));

        @Test
        public void conversions() {
            readsAs(value, List.of(Character.class, char.class), is('a'));
            readsAs(value, List.of(String.class), is("a"));

            doesNotReadAs(value, List.of(
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class
            ));
        }
    }

    public static class OfSingleNumericCharacterString {
        JsonElementValue value = new JsonElementValue(gson, new JsonPrimitive("8"));

        @Test
        public void conversions() {
            readsAs(value, List.of(Short.class, short.class), is((short) 8));
            readsAs(value, List.of(Integer.class, int.class), is(8));
            readsAs(value, List.of(Long.class, long.class), is((long) 8));
            readsAs(value, List.of(Float.class, float.class), is(8f));
            readsAs(value, List.of(Double.class, double.class), is(8d));
            readsAs(value, List.of(Character.class, char.class), is('8'));
            readsAs(value, List.of(String.class), is("8"));
        }
    }

    public static class OfObject {
        JsonElementValue value = new JsonElementValue(gson, gson.toJsonTree(
                new DummyObject("some value", 123))
        );

        @Test
        public void conversions() {
            readsAs(value, DummyObject.class, allOf(
                    hasProperty("attribute1", is("some value")),
                    hasProperty("attribute2", is(123))
            ));

            doesNotReadAs(value, List.of(
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class,
                    Character.class, char.class,
                    String.class
            ));
        }

        public static class DummyObject {
            private String attribute1;
            private int attribute2;

            public DummyObject(String attribute1, int attribute2) {
                this.attribute1 = attribute1;
                this.attribute2 = attribute2;
            }

            public String getAttribute1() {
                return attribute1;
            }

            public int getAttribute2() {
                return attribute2;
            }
        }
    }

    public static class OfIntegerArray {
        JsonElementValue value = new JsonElementValue(gson, toJsonArray(new int[]{1, 2, 3, 4, 5}));

        @Test
        public void conversions() throws NoSuchMethodException {
            readsAs(value, int[].class, is(new int[]{1, 2, 3, 4, 5}));
            readsAs(value, Integer[].class, arrayContaining(1, 2, 3, 4, 5));

            Type listOfIntegers = listOfIntegers();
            assertThat("Conversion to " + listOfIntegers, value.readAs(listOfIntegers), is(List.of(1, 2, 3, 4, 5)));
            assertTrue("Should be compatible with " + listOfIntegers, value.isCompatibleWith(listOfIntegers));

            doesNotReadAs(value, List.of(
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class,
                    Character.class, char.class,
                    String.class
            ));
        }

        private static JsonArray toJsonArray(int[] value) {
            JsonArray array = new JsonArray(value.length);
            Arrays.stream(value).forEach(array::add);
            return array;
        }

        private Type listOfIntegers() throws NoSuchMethodException {
            return getClass().getMethod("dummyMethod").getGenericReturnType();
        }

        public List<Integer> dummyMethod() {
            return List.of();
        }
    }

    private static <T> void readsAs(Value value, List<Class<T>> types, Matcher<? super T> matches) {
        for (Class<T> type : types) {
            readsAs(value, type, matches);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void readsAs(Value value, Class<T> type, Matcher<? super T> matches) {
        assertThat("Conversion to " + type, (T) value.readAs(type), matches);
        assertTrue("Should be compatible with " + type, value.isCompatibleWith(type));
    }

    private static void doesNotReadAs(Value value, List<Class<?>> types) {
        for (Class<?> type : types) {
            assertFalse("Should not be compatible with " + type, value.isCompatibleWith(type));
        }
    }
}