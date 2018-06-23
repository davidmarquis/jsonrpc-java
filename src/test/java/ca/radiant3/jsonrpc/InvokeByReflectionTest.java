package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.server.InvokeByReflection;
import org.junit.Test;

import static ca.radiant3.jsonrpc.testkit.ResultThat.hasValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class InvokeByReflectionTest {

    @Test
    public void withoutParameter() throws Exception {
        InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

        Result result = handler.handle(Invocation.of("noParameter"));

        assertThat(result, hasValue(equalTo("some value")));
    }

    @Test
    public void singleParameter() throws Exception {
        InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

        Result result = handler.handle(Invocation.of("singleStringParameter")
                                                 .withParameter(new Param(Value.of("hello world"))));

        assertThat(result, hasValue(equalTo("hello world")));
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
