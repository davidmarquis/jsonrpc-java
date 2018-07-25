package ca.radiant3.jsonrpc;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static ca.radiant3.jsonrpc.testkit.InvocationThat.hasSameState;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class SignatureTest {

    public static class CreationFromMethod {
        @Test
        public void withoutParameter() throws NoSuchMethodException {
            Method method = Examples.class.getMethod("noParam");

            Signature signature = Signature.of(method);

            assertThat(signature.getMethodName(), equalTo("noParam"));
            assertThat(signature.parameters(), hasSize(0));
        }

        @Test
        public void withSingleParameter() throws NoSuchMethodException {
            Method method = Examples.class.getMethod("singleParam", String.class);

            Signature signature = Signature.of(method);

            assertThat(signature.getMethodName(), equalTo("singleParam"));
            assertThat(signature.parameters(), hasSize(1));
            assertThat(signature.parameters().get(0).getName(), nullValue());
            assertThat(signature.parameters().get(0).getType(), equalTo(String.class));
        }

        @Test
        public void withMultipleParameters() throws NoSuchMethodException {
            Method method = Examples.class.getMethod("multipleParams", String.class, int.class, boolean.class);

            Signature signature = Signature.of(method);

            assertThat(signature.getMethodName(), equalTo("multipleParams"));
            assertThat(signature.parameters(), hasSize(3));

            assertThat(signature.parameters().get(0).getName(), nullValue());
            assertThat(signature.parameters().get(0).getType(), equalTo(String.class));

            assertThat(signature.parameters().get(1).getName(), nullValue());
            assertThat(signature.parameters().get(1).getType(), equalTo(int.class));

            assertThat(signature.parameters().get(2).getName(), nullValue());
            assertThat(signature.parameters().get(2).getType(), equalTo(boolean.class));
        }

        @Test
        public void withListParameter() throws NoSuchMethodException {
            Method method = Examples.class.getMethod("listParam", List.class);

            Signature signature = Signature.of(method);

            Type paramType = signature.parameters().get(0).getType();
            assertThat(((ParameterizedType) paramType).getActualTypeArguments(), arrayContaining(String.class));
        }

        @Test
        public void includesReturnType() throws NoSuchMethodException {
            Method method = Examples.class.getMethod("withReturnValue");

            Signature signature = Signature.of(method);

            assertThat(signature.getReturnType(), equalTo(String.class));
        }
    }

    public static class ConversionToInvocation {
        @Test
        public void withoutParameter() throws NoSuchMethodException {
            Signature signature = Signature.of(Examples.class.getMethod("noParam"));

            Invocation invocation = signature.toInvocation(List.of());

            assertThat(invocation, hasSameState(Invocation.of("noParam")));
        }

        @Test
        public void withSingleParameter() throws NoSuchMethodException {
            Signature signature = Signature.of(Examples.class.getMethod("singleParam", String.class));

            Invocation invocation = signature.toInvocation(List.of("value"));


            assertThat(invocation, hasSameState(Invocation.of("singleParam")
                                                          .withParameter(Arg.of(Value.of("value")))));
        }

        @Test
        public void withMultipleParameters() throws NoSuchMethodException {
            Signature signature = Signature.of(Examples.class.getMethod("multipleParams", String.class, int.class, boolean.class));

            Invocation invocation = signature.toInvocation(List.of("value", 123, true));


            assertThat(invocation, hasSameState(Invocation.of("multipleParams")
                                                          .withParameter(Arg.of(Value.of("value")))
                                                          .withParameter(Arg.of(Value.of(123)))
                                                          .withParameter(Arg.of(Value.of(true)))
            ));
        }

        @Test
        public void withNamedParameters() throws NoSuchMethodException {
            Signature signature = Signature.of(Examples.class.getMethod("namedParams", String.class, int.class));

            Invocation invocation = signature.toInvocation(List.of("value", 123));


            assertThat(invocation, hasSameState(Invocation.of("namedParams")
                                                          .withParameter(Arg.of(Value.of("value")).named("first"))
                                                          .withParameter(Arg.of(Value.of(123)).named("second"))
            ));
        }
    }

    interface Examples {
        void noParam();
        void singleParam(String param);
        void multipleParams(String param1, int param2, boolean param3);
        void listParam(List<String> param);
        String withReturnValue();

        void namedParams(@Named("first") String param1, @Named("second") int param2);
    }
}