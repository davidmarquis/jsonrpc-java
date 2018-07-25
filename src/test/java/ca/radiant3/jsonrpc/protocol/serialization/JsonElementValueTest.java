package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.protocol.serialization.gson.JsonElementValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

import static ca.radiant3.jsonrpc.protocol.serialization.JsonElementValueTest.ValueAssertions.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class JsonElementValueTest {
    private static Gson gson = new Gson();

    public static class OfBoolean {
        JsonElement aBoolean = new JsonPrimitive(true);

        @Test
        public void conversions() {
            valueOf(aBoolean).whenReadAs(Boolean.class, boolean.class, equalTo(true));
            valueOf(aBoolean).whenReadAs(String.class, equalTo("true"));

            valueOf(aBoolean).doesNotReadAs(
                    Byte.class, byte.class,
                    Character.class, char.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class
            );
        }
    }

    public static class OfSingleDigitInteger {
        JsonElement singleDigitInteger = new JsonPrimitive(5);

        @Test
        public void conversions() {
            valueOf(singleDigitInteger).whenReadAs(Byte.class, byte.class, is((byte) 5));
            valueOf(singleDigitInteger).whenReadAs(Character.class, char.class, equalTo('5'));
            valueOf(singleDigitInteger).whenReadAs(Short.class, short.class, is((short) 5));
            valueOf(singleDigitInteger).whenReadAs(Integer.class, int.class, is(5));
            valueOf(singleDigitInteger).whenReadAs(Long.class, long.class, is((long) 5));
            valueOf(singleDigitInteger).whenReadAs(Float.class, float.class, is(5f));
            valueOf(singleDigitInteger).whenReadAs(Double.class, double.class, is(5d));
            valueOf(singleDigitInteger).whenReadAs(String.class, is("5"));

            valueOf(singleDigitInteger).doesNotReadAs(Boolean.class, boolean.class);
        }
    }

    public static class OfShortSizedInteger {
        JsonElement smallInteger = new JsonPrimitive(123);

        @Test
        public void conversions() {
            valueOf(smallInteger).whenReadAs(Byte.class, byte.class, is((byte) 123));
            valueOf(smallInteger).whenReadAs(Short.class, short.class, is((short) 123));
            valueOf(smallInteger).whenReadAs(Integer.class, int.class, is(123));
            valueOf(smallInteger).whenReadAs(Long.class, long.class, is((long) 123));
            valueOf(smallInteger).whenReadAs(Float.class, float.class, is(123f));
            valueOf(smallInteger).whenReadAs(Double.class, double.class, is(123d));
            valueOf(smallInteger).whenReadAs(String.class, is("123"));

            valueOf(smallInteger).doesNotReadAs(
                    Character.class, char.class,
                    Boolean.class, boolean.class
            );
        }
    }

    public static class OfLongSizedInteger {
        JsonElement largeInteger = new JsonPrimitive(991882123);

        @Test
        public void conversions() {
            valueOf(largeInteger).whenReadAs(Byte.class, byte.class, is((byte) -117));  // overflows
            valueOf(largeInteger).whenReadAs(Short.class, short.class, is((short) -5237));  // overflows
            valueOf(largeInteger).whenReadAs(Integer.class, int.class, is(991882123));
            valueOf(largeInteger).whenReadAs(Long.class, long.class, is((long) 991882123));
            valueOf(largeInteger).whenReadAs(Float.class, float.class, is(991882123f));
            valueOf(largeInteger).whenReadAs(Double.class, double.class, is(991882123d));
            valueOf(largeInteger).whenReadAs(String.class, is("991882123"));

            valueOf(largeInteger).doesNotReadAs(
                    Character.class, char.class,
                    Boolean.class, boolean.class
            );
        }
    }

    @RunWith(Parameterized.class)
    public static class OfDecimals {
        private static final float FLOAT = 345.19f;
        private static final double DOUBLE = 345.19d;

        @Parameterized.Parameters
        public static Collection<JsonElement> subjects() {
            return asList(
                    new JsonPrimitive(FLOAT),
                    new JsonPrimitive(DOUBLE)
            );
        }

        @Parameterized.Parameter
        public JsonElement decimal;

        @Test
        public void conversions() {
            valueOf(decimal).whenReadAs(Byte.class, byte.class, is((byte) 345));
            valueOf(decimal).whenReadAs(Short.class, short.class, is((short) 345));
            valueOf(decimal).whenReadAs(Integer.class, int.class, is(345));
            valueOf(decimal).whenReadAs(Long.class, long.class, is((long) 345));
            valueOf(decimal).whenReadAs(Float.class, float.class, is(345.19f));
            valueOf(decimal).whenReadAs(Double.class, double.class, closeTo(345.19d, 0.001d));
            valueOf(decimal).whenReadAs(String.class, is("345.19"));

            valueOf(decimal).doesNotReadAs(
                    Character.class, char.class,
                    Boolean.class, boolean.class
            );
        }
    }

    public static class OfBooleanString {
        JsonElement nonNumericString = new JsonPrimitive("true");

        @Test
        public void conversions() {
            valueOf(nonNumericString).whenReadAs(Boolean.class, boolean.class, is(true));
            valueOf(nonNumericString).whenReadAs(String.class, is("true"));

            valueOf(nonNumericString).doesNotReadAs(
                    Byte.class, byte.class,
                    Character.class, char.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class
            );
        }
    }

    public static class OfNonNumericString {
        JsonElement nonNumericString = new JsonPrimitive("abc");

        @Test
        public void conversions() {
            valueOf(nonNumericString).whenReadAs(Boolean.class, boolean.class, is(false));
            valueOf(nonNumericString).whenReadAs(String.class, is("abc"));

            valueOf(nonNumericString).doesNotReadAs(
                    Byte.class, byte.class,
                    Character.class, char.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class
            );
        }
    }

    public static class OfNumericDecimalString {
        JsonElement numericDecimalString = new JsonPrimitive("199.19");

        @Test
        public void conversions() {
            valueOf(numericDecimalString).whenReadAs(Boolean.class, boolean.class, is(false));
            valueOf(numericDecimalString).whenReadAs(Float.class, float.class, is(199.19f));
            valueOf(numericDecimalString).whenReadAs(Double.class, double.class, allOf(
                    greaterThanOrEqualTo(199.19d), lessThan(199.2d)
            ));
            valueOf(numericDecimalString).whenReadAs(String.class, is("199.19"));

            valueOf(numericDecimalString).doesNotReadAs(
                    Byte.class, byte.class,
                    Character.class, char.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class
            );
        }
    }

    public static class OfNumericIntegerString {
        JsonElement numericIntegerString = new JsonPrimitive("199");

        @Test
        public void conversions() {
            valueOf(numericIntegerString).whenReadAs(Byte.class, byte.class, is((byte) 199));
            valueOf(numericIntegerString).whenReadAs(Short.class, short.class, is((short) 199));
            valueOf(numericIntegerString).whenReadAs(Integer.class, int.class, is(199));
            valueOf(numericIntegerString).whenReadAs(Long.class, long.class, is((long) 199));
            valueOf(numericIntegerString).whenReadAs(Float.class, float.class, is(199f));
            valueOf(numericIntegerString).whenReadAs(Double.class, double.class, is(199d));
            valueOf(numericIntegerString).whenReadAs(String.class, is("199"));

            valueOf(numericIntegerString).doesNotReadAs(
                    Character.class, char.class
            );
        }
    }

    public static class OfSingleCharacterString {
        JsonPrimitive singleCharacterString = new JsonPrimitive("a");

        @Test
        public void conversions() {
            valueOf(singleCharacterString).whenReadAs(Boolean.class, boolean.class, is(false));
            valueOf(singleCharacterString).whenReadAs(Character.class, char.class, is('a'));
            valueOf(singleCharacterString).whenReadAs(String.class, is("a"));

            valueOf(singleCharacterString).doesNotReadAs(
                    Byte.class, byte.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class
            );
        }
    }

    public static class OfSingleNumericCharacterString {
        JsonElement singleDigitIntegerString = new JsonPrimitive("8");

        @Test
        public void conversions() {
            valueOf(singleDigitIntegerString).whenReadAs(Boolean.class, boolean.class, is(false));
            valueOf(singleDigitIntegerString).whenReadAs(Byte.class, byte.class, is((byte) 8));
            valueOf(singleDigitIntegerString).whenReadAs(Character.class, char.class, is('8'));
            valueOf(singleDigitIntegerString).whenReadAs(Short.class, short.class, is((short) 8));
            valueOf(singleDigitIntegerString).whenReadAs(Integer.class, int.class, is(8));
            valueOf(singleDigitIntegerString).whenReadAs(Long.class, long.class, is((long) 8));
            valueOf(singleDigitIntegerString).whenReadAs(Float.class, float.class, is(8f));
            valueOf(singleDigitIntegerString).whenReadAs(Double.class, double.class, is(8d));
            valueOf(singleDigitIntegerString).whenReadAs(String.class, is("8"));
        }
    }

    public static class OfObject {
        JsonElement object = gson.toJsonTree(new DummyObject("some value", 123));

        @Test
        public void conversions() {
            valueOf(object).whenReadAs(DummyObject.class, allOf(
                    hasProperty("attribute1", is("some value")),
                    hasProperty("attribute2", is(123))
            ));

            valueOf(object).doesNotReadAs(
                    Boolean.class, boolean.class,
                    Byte.class, byte.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class,
                    Character.class, char.class,
                    String.class
            );
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
        JsonElement arrayOfIntegers = toJsonArray(new int[]{1, 2, 3, 4, 5});

        @Test
        public void conversions() throws NoSuchMethodException {
            valueOf(arrayOfIntegers).whenReadAs(int[].class, is(new int[]{1, 2, 3, 4, 5}));
            valueOf(arrayOfIntegers).whenReadAs(Integer[].class, arrayContaining(1, 2, 3, 4, 5));
            valueOf(arrayOfIntegers).whenReadAsType(listOfIntegers(), is(List.of(1, 2, 3, 4, 5)));

            valueOf(arrayOfIntegers).doesNotReadAs(
                    Boolean.class, boolean.class,
                    Byte.class, byte.class,
                    Short.class, short.class,
                    Integer.class, int.class,
                    Long.class, long.class,
                    Float.class, float.class,
                    Double.class, double.class,
                    Character.class, char.class,
                    String.class
            );
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

    static class ValueAssertions {
        private Value subject;

        private ValueAssertions(Value subject) {
            this.subject = subject;
        }

        public static ValueAssertions valueOf(JsonElement element) {
            return new ValueAssertions(new JsonElementValue(gson, element));
        }

        <T> void whenReadAs(Class<T> type1, Class<T> type2, Matcher<? super T> matches) {
            for (Class<T> type : asList(type1, type2)) {
                whenReadAs(type, matches);
            }
        }

        @SuppressWarnings("unchecked")
        <T> void whenReadAs(Class<T> type, Matcher<? super T> matches) {
            whenReadAsType(type, (Matcher<? super Object>) matches);
        }

        void whenReadAsType(Type type, Matcher<? super Object> matches) {
            assertThat("Conversion to " + type, subject.readAs(type), matches);
            assertTrue("Should be compatible with " + type, subject.isCompatibleWith(type));
        }

        void doesNotReadAs(Class<?>... types) {
            for (Class<?> type : types) {
                assertFalse("Should not be compatible with " + type, subject.isCompatibleWith(type));
            }
        }
    }
}