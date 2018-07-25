package ca.radiant3.jsonrpc.json;

import ca.radiant3.jsonrpc.Arg;
import ca.radiant3.jsonrpc.Args;
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

    public static ParametersJson unnamed(Value... values) {
        ParametersJson params = new ParametersJson();
        Arrays.stream(values).forEach(params::add);
        return params;
    }

    public static ParametersJson unnamed(List<Value> values) {
        ParametersJson params = new ParametersJson();
        values.forEach(params::add);
        return params;
    }

    public static ParametersJson named() {
        return new ParametersJson();
    }

    public static ParametersJson of(Args arguments) {
        ParametersJson result = ParametersJson.none();
        result.params = arguments.list().stream()
                                 .map(Parameter::fromParam)
                                 .collect(toList());
        return result;
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

    public void addTo(Args arguments) {
        params.forEach(p -> arguments.add(p.toParam()));
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

        public Arg toParam() {
            Arg arg = Arg.of(value);
            if (name != null) {
                arg = arg.named(name);
            }
            return arg;
        }

        public static Parameter fromParam(Arg arg) {
            return new Parameter(arg.getValue()).named(arg.getName());
        }
    }
}
