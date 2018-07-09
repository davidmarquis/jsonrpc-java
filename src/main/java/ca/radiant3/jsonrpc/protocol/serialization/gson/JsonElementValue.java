package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.Optional;

public class JsonElementValue implements Value {
    private JsonElement element;
    private Gson gson;

    public JsonElementValue(Gson gson, JsonElement element) {
        this.element = element;
        this.gson = gson;
    }

    @Override
    public Optional<Type> getType() {
        return Optional.empty();  // actual type is unknown
    }

    @Override
    public Object readAs(Type type) {
        try {
            return gson.fromJson(element, type);
        } catch (JsonSyntaxException | NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }
}
