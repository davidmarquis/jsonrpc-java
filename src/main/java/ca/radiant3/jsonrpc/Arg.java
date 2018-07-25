package ca.radiant3.jsonrpc;

import java.util.Objects;

public class Arg {
    private final Value value;
    private String name;

    private Arg(Value value) {
        this.value = value;
    }

    public static Arg of(Value value) {
        assert value != null;
        return new Arg(value);
    }

    public Arg named(String name) {
        this.name = name;
        return this;
    }

    public Value getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String result = isNamed() ? name + ":" : "";
        result += value.toString();
        return result;
    }

    private boolean isNamed() {
        return name != null && !name.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arg arg = (Arg) o;
        return Objects.equals(value, arg.value) &&
                Objects.equals(name, arg.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, name);
    }
}
