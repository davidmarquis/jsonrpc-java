package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Param;
import ca.radiant3.jsonrpc.Parameters;
import ca.radiant3.jsonrpc.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ParametersJson {
    private List<Parameter> params = new ArrayList<>();

    private ParametersJson() {}

    public static ParametersJson none() {
        return new ParametersJson();
    }

    public static ParametersJson of(Value... values) {
        ParametersJson params = new ParametersJson();
        Arrays.stream(values).forEach(params::add);
        return params;
    }

    public static ParametersJson of(List<Value> values) {
        ParametersJson params = new ParametersJson();
        values.forEach(params::add);
        return params;
    }

    public static ParametersJson named() {
        return new ParametersJson();
    }

    private void add(Value unnamed) {
        params.add(new Parameter(unnamed));
    }

    public ParametersJson add(String name, Value value) {
        params.add(new Parameter(value).named(name));
        return this;
    }

    public List<Value> values() {
        return params.stream()
                     .map(Parameter::getValue)
                     .collect(toList());
    }

    public Map<String, Value> toMap() {
        return params.stream()
                     .filter(Parameter::isNamed)
                     .collect(Collectors.toMap(Parameter::getName, Parameter::getValue));
    }

    public boolean hasNamedParameters() {
        return params.stream().anyMatch(Parameter::isNamed);
    }

    public void addTo(Parameters parameters) {
        params.forEach(p -> parameters.addParameter(p.toParam()));
    }

    public int count() {
        return params.size();
    }

    public static class Parameter {
        private final Value value;
        private String name = null;

        private Parameter(Value value) {
            this.value = value;
        }

        private Parameter named(String name) {
            this.name = name;
            return this;
        }

        public boolean isNamed() {
            return name != null;
        }

        public String getName() {
            return name;
        }

        public Value getValue() {
            return value;
        }

        public Param toParam() {
            Param param = new Param(value);
            if (name != null) {
                param = param.named(name);
            }
            return param;
        }
    }
}
