package ca.radiant3.jsonrpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parameters {
    private List<Param> parameters = new ArrayList<>();

    public void addParameter(Param parameter) {
        parameters.add(parameter);
    }

    public List<Param> list() {
        return Collections.unmodifiableList(parameters);
    }

    public int count() {
        return parameters.size();
    }
}
