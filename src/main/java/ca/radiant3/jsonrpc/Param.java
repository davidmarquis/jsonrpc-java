package ca.radiant3.jsonrpc;

import java.util.Optional;

public class Param {
    private final Value value;
    private String name;

    public Param(Value value) {
        this.value = value;
    }

    public Param named(String name) {
        this.name = name;
        return this;
    }

    public Value getValue() {
        return value;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }
}
