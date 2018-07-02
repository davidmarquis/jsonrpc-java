package ca.radiant3.jsonrpc;

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

    public String getName() {
        return name;
    }
}
