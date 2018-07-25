package ca.radiant3.jsonrpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Args {
    private List<Arg> parameters = new ArrayList<>();

    public static Args of(Arg... args) {
        Args result = new Args();
        Arrays.stream(args).forEach(result::add);
        return result;
    }

    public static Args of(Value... values) {
        Args result = new Args();
        Arrays.stream(values).forEach(result::add);
        return result;
    }

    public static Args none() {
        return new Args();
    }

    public Args add(Arg parameter) {
        parameters.add(parameter);
        return this;
    }

    private Args add(Value value) {
        parameters.add(Arg.of(value));
        return this;
    }

    public List<Arg> list() {
        return Collections.unmodifiableList(parameters);
    }

    public int count() {
        return parameters.size();
    }
}
