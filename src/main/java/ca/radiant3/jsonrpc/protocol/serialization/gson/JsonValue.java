package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

class JsonValue implements Value {
    private JsonElement element;
    private Gson gson;

    JsonValue(Gson gson, JsonElement element) {
        this.element = element;
        this.gson = gson;
    }

    @Override
    public Object readAs(Type type) {
        return gson.fromJson(element, type);
    }

    @Override
    public Type type() {
        return null;
    }
}
