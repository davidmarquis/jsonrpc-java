package ca.radiant3.jsonrpc;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ArgTest {
    @Test
    public void toStringForUnnamedParameter() {
        Arg arg = Arg.of(Value.of(123));

        assertThat(arg.toString(), is(Value.of(123).toString()));
    }

    @Test
    public void toStringForNamedParameter() {
        Arg arg = Arg.of(Value.of(123))
                     .named("param");

        assertThat(arg.toString(), is(String.format("param:%s", Value.of(123))));
    }
}