package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.server.InvokeByReflection;
import org.junit.Test;

import java.util.List;

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
    public void singleParameterString() throws Exception {
        InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

        Value result = handler.handle(Invocation.of("duplicate")
                                                       .withParameter(new Param(Value.of("hello world"))));

        assertThat(result, readsAsString("hello worldhello world"));
    }

    @Test
    public void singleParameterInteger() throws Exception {
        InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

        Value result = handler.handle(Invocation.of("duplicate")
                                                       .withParameter(new Param(Value.of(12))));

        assertThat(result, readsAsString("24"));
    }

    @Test
    public void listOfStringsParameter() throws Exception {
        InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

        Value result = handler.handle(Invocation.of("join")
                                                       .withParameter(new Param(Value.of(List.of("h", "e", "l", "l", "o")))));

        assertThat(result, readsAsString("hello"));
    }

    public interface MyService {
        String noParameter();

        String duplicate(String param);
        String duplicate(Integer param);

        String join(List<String> items);
    }

    public static class MyServiceImpl implements MyService {
        @Override
        public String noParameter() {
            return "some value";
        }

        @Override
        public String duplicate(String param) {
            return param + param;
        }

        public String duplicate(Integer param) {
            return String.valueOf(param + param);
        }

        @Override
        public String join(List<String> items) {
            return String.join("", items);
        }
    }
}
