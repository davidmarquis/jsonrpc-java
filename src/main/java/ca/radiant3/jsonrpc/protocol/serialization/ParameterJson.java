package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Value;

import java.util.Optional;

public class ParameterJson {
    private final Value value;
    private String name;

    public ParameterJson(Value value) {
        this.value = value;
    }

    public ParameterJson named(String name) {
        this.name = name;
        return this;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Value getValue() {
        return value;
    }
}
