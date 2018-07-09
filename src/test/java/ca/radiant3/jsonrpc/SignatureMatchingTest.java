package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.server.Signature;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class SignatureMatchingTest {
    private interface Example {
        void noParam();
        void singleParam(String param);
        void multipleParams(String param1, int param2);
    }

    public static class NoParameter {
        private Signature signature;

        @Before
        public void withSignature() throws NoSuchMethodException {
            signature = Signature.of(Example.class.getMethod("noParam"));
        }

        private Invocation invocationWith(Param... params) {
            Invocation invocation = Invocation.of("noParam");
            Arrays.stream(params).forEach(invocation::withParameter);
            return invocation;
        }

        @Test
        public void matches() {
            assertTrue(signature.matches(
                    invocationWith()
            ));
        }

        @Test
        public void doesNotMatchWhenAnyParameterPresent() {
            assertFalse(signature.matches(
                    invocationWith(new Param(Value.of("extra parameter")))
            ));
        }
    }

    public static class SingleParameter {
        private Signature signature;

        @Before
        public void withSignature() throws NoSuchMethodException {
            signature = Signature.of(Example.class.getMethod("singleParam", String.class));
        }

        private Invocation invocationWith(Param... params) {
            Invocation invocation = Invocation.of("singleParam");
            Arrays.stream(params).forEach(invocation::withParameter);
            return invocation;
        }

        @Test
        public void matches() throws NoSuchMethodException {
            Method method = Example.class.getMethod("singleParam", String.class);

            assertTrue(Signature.of(method).matches(
                    invocationWith(new Param(Value.of("param")))
            ));
        }

        @Test
        public void doesNotMatchWhenNoParameterPresent() {
            assertFalse(signature.matches(
                    invocationWith()
            ));
        }
    }

    public static class MultipleParameters {
        private Signature signature;

        @Before
        public void withSignature() throws NoSuchMethodException {
            signature = Signature.of(Example.class.getMethod("multipleParams", String.class, int.class));
        }

        private Invocation invocationWith(Param... params) {
            Invocation invocation = Invocation.of("multipleParams");
            Arrays.stream(params).forEach(invocation::withParameter);
            return invocation;
        }

        @Test
        public void matches() {
            assertTrue(signature.matches(
                    invocationWith(new Param(Value.of("param")), new Param(Value.of(123)))
            ));
        }

        @Test
        public void doesNotMatchWhenTypeIsIncompatible() {
            assertFalse(signature.matches(
                    invocationWith(new Param(Value.of("param")), new Param(Value.of("wrong type")))
            ));
        }

        @Test
        public void doesNotMatchWhenTooFewParameters() {
            assertFalse(signature.matches(
                    invocationWith(new Param(Value.of("only one parameter")))
            ));
        }

        @Test
        public void doesNotMatchWhenTooManyParameters() {
            assertFalse(signature.matches(
                    invocationWith(new Param(Value.of("param")), new Param(Value.of(123)), new Param(Value.of("extra parameter"))))
            );
        }
    }
}