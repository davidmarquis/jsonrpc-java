package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.server.Signature;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SignatureTest {
    @Test
    public void noParameter() throws NoSuchMethodException {
        Method method = Example.class.getMethod("noParam");

        Signature signature = Signature.of(method);

        assertThat(signature.methodName(), equalTo("noParam"));
        assertThat(signature.parameters(), hasSize(0));
    }

    @Test
    public void singleParameter() throws NoSuchMethodException {
        Method method = Example.class.getMethod("singleParam", String.class);

        Signature signature = Signature.of(method);

        assertThat(signature.methodName(), equalTo("singleParam"));
        assertThat(signature.parameters(), hasSize(1));
        assertThat(signature.parameters().get(0).getName(), nullValue());
        assertThat(signature.parameters().get(0).getType(), equalTo(String.class));
    }

    @Test
    public void multipleParameters() throws NoSuchMethodException {
        Method method = Example.class.getMethod("multipleParams", String.class, int.class, boolean.class);

        Signature signature = Signature.of(method);

        assertThat(signature.methodName(), equalTo("multipleParams"));
        assertThat(signature.parameters(), hasSize(3));

        assertThat(signature.parameters().get(0).getName(), nullValue());
        assertThat(signature.parameters().get(0).getType(), equalTo(String.class));

        assertThat(signature.parameters().get(1).getName(), nullValue());
        assertThat(signature.parameters().get(1).getType(), equalTo(int.class));

        assertThat(signature.parameters().get(2).getName(), nullValue());
        assertThat(signature.parameters().get(2).getType(), equalTo(boolean.class));
    }

    @Test
    public void withResult() throws NoSuchMethodException {
        Method method = Example.class.getMethod("withResult");

        Signature signature = Signature.of(method);

        assertThat(signature.getReturnType(), equalTo(String.class));
    }

    @Test
    public void withListParameter() throws NoSuchMethodException {
        Method method = Example.class.getMethod("listParam", List.class);

        Signature signature = Signature.of(method);

        Type paramType = signature.parameters().get(0).getType();
        assertThat(((ParameterizedType) paramType).getActualTypeArguments(), arrayContaining(String.class));
    }

    private interface Example {
        void noParam();
        void singleParam(String param);
        void multipleParams(String param1, int param2, boolean param3);
        void listParam(List<String> param);
        String withResult();
    }
}