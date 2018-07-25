package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.Value;

import java.util.Random;
import java.util.function.IntSupplier;

public class ValueBuilder {
    private static IntSupplier generator = () -> new Random().nextInt();

    public static Value anyInt() {
        return Value.of(generator.getAsInt());
    }

    public static Value anyString() {
        return Value.of(Integer.toString(generator.getAsInt()));
    }
}
