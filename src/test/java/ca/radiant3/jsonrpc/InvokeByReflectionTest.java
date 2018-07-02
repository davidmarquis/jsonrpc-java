package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.server.InvokeByReflection;
import org.junit.Test;

import static ca.radiant3.jsonrpc.testkit.ValueThat.readsAsString;
import static org.junit.Assert.assertThat;

public class InvokeByReflectionTest {

    @Test
    public void withoutParameter() throws Exception {
        InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

        Value result = handler.handle(Invocation.of("noParameter"));

        assertThat(result, readsAsString("some value"));
    }

    @Test
    public void singleParameter() throws Exception {
        InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

        Value result = handler.handle(Invocation.of("singleStringParameter")
                                                 .withParameter(new Param(Value.of("hello world"))));

        assertThat(result, readsAsString("hello world"));
    }

    public interface MyService {
        String noParameter();

        String singleStringParameter(String param);
    }

    public static class MyServiceImpl implements MyService {
        @Override
        public String noParameter() {
            return "some value";
        }

        @Override
        public String singleStringParameter(String param) {
            return param;
        }
    }
}
