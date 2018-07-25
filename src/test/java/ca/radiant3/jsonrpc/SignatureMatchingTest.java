package ca.radiant3.jsonrpc;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static ca.radiant3.jsonrpc.Signature.hasParameters;
import static ca.radiant3.jsonrpc.testkit.ValueBuilder.anyInt;
import static ca.radiant3.jsonrpc.testkit.ValueBuilder.anyString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class SignatureMatchingTest {
    private interface Example {
        void noParam();

        void singleParam(String param);

        void multipleParams(String param1, int param2);
    }

    public static class ByName {
        @Test
        public void matches() throws NoSuchMethodException {
            Signature signature = Signature.of(Example.class.getMethod("noParam"));

            assertTrue(Signature.hasMethodName("noParam").test(signature));
            assertFalse(Signature.hasMethodName("unknownName").test(signature));
        }
    }

    public static class ByArguments {
        Args none = Args.none();
        Args one = Args.of(anyString());
        Args two = Args.of(anyString(), anyInt());

        @Test
        public void matchesForEmptyParameters() throws NoSuchMethodException {
            Signature signature = Signature.of(Example.class.getMethod("noParam"));

            assertTrue(hasParameters(none).test(signature));
            assertFalse(hasParameters(one).test(signature));
            assertFalse(hasParameters(two).test(signature));
        }

        @Test
        public void matchesForSingleParameters() throws NoSuchMethodException {
            Signature signature = Signature.of(Example.class.getMethod("singleParam", String.class));

            assertFalse(hasParameters(none).test(signature));
            assertTrue(hasParameters(one).test(signature));
            assertFalse(hasParameters(two).test(signature));
        }

        @Test
        public void matchesForMultipleParameters() throws NoSuchMethodException {
            Signature signature = Signature.of(Example.class.getMethod("multipleParams", String.class, int.class));

            assertFalse(hasParameters(none).test(signature));
            assertFalse(hasParameters(one).test(signature));
            assertTrue(hasParameters(two).test(signature));
        }
    }
}