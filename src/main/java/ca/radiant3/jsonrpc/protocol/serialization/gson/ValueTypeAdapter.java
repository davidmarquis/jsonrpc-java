package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ValueTypeAdapter extends TypeAdapter<Value> {
    private final Gson gson;

    public ValueTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Value value) {
        value.getType().ifPresentOrElse(
                (type) -> gson.toJson(value.readAs(type), type, out),
                () -> {
                    throw new UnsupportedOperationException("Cannot write a Value of an unknown type");
                });
    }

    @Override
    public Value read(JsonReader in) {
        throw new UnsupportedOperationException("ValueTypeAdapter can only be used to write values");
    }
}
