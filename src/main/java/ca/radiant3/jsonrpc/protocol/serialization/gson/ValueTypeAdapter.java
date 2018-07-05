package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class ValueTypeAdapter extends TypeAdapter<Value> {
    private final Gson gson;

    public ValueTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Value value) throws IOException {
        gson.toJson(value.get(), value.type(), out);
    }

    @Override
    public Value read(JsonReader in) throws IOException {
        return null;
    }
}
