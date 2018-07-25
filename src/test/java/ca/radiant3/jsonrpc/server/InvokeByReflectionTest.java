package ca.radiant3.jsonrpc.server;

import ca.radiant3.jsonrpc.Arg;
import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Value;
import org.junit.Test;

import java.util.List;

import static ca.radiant3.jsonrpc.testkit.ValueThat.readsAsString;
import static org.junit.Assert.assertThat;

public class InvokeByReflectionTest {
    InvokeByReflection handler = new InvokeByReflection(MyService.class, new MyServiceImpl());

    @Test
    public void withoutParameter() throws Exception {
        Value result = handler.handle(Invocation.of("noParameter"));

        assertThat(result, readsAsString("some value"));
    }

    @Test
    public void singleParameterString() throws Exception {
        Value result = handler.handle(Invocation.of("duplicate")
                                                .withArgument(Arg.of(Value.of("hello world"))));

        assertThat(result, readsAsString("hello worldhello world"));
    }

    @Test
    public void singleParameterInteger() throws Exception {
        Value result = handler.handle(Invocation.of("duplicate")
                                                .withArgument(Arg.of(Value.of(12))));

        assertThat(result, readsAsString("24"));
    }

    @Test
    public void listOfStringsParameter() throws Exception {
        Value result = handler.handle(Invocation.of("join")
                                                .withArgument(Arg.of(Value.of(List.of("h", "e", "l", "l", "o")))));

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
