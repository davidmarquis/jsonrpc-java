package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.json.ErrorJson;
import ca.radiant3.jsonrpc.json.InvocationJson;
import ca.radiant3.jsonrpc.json.ResponseJson;
import ca.radiant3.jsonrpc.protocol.serialization.PayloadSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;

public class GsonPayloadSerializer implements PayloadSerializer {
    private final Gson gson;

    public GsonPayloadSerializer() {
        this(new GsonBuilder());
    }

    public GsonPayloadSerializer(GsonBuilder builder) {
        Gson baseGson = builder
                .serializeNulls()
                .registerTypeAdapterFactory(new ValueTypeAdapterFactory())
                .registerTypeAdapter(ErrorJson.class, new ErrorTypeAdapter(builder.create()))
                .create();

        gson = builder
                .registerTypeAdapter(InvocationJson.class, new InvocationTypeAdapter(baseGson))
                .registerTypeAdapter(ResponseJson.class, new ResponseTypeAdapter(baseGson))
                .create();
    }

    @Override
    public void serialize(InvocationJson invocation, OutputStream out) throws IOException {
        JsonWriter writer = gson.newJsonWriter(new OutputStreamWriter(out));
        gson.toJson(invocation, InvocationJson.class, writer);
    }

    @Override
    public void serialize(ResponseJson response, OutputStream out) throws IOException {
        JsonWriter writer = gson.newJsonWriter(new OutputStreamWriter(out));
        gson.toJson(response, ResponseJson.class, writer);
    }

    @Override
    public InvocationJson readInvocation(InputStream json) {
        JsonReader reader = new JsonReader(new InputStreamReader(json));
        return gson.fromJson(reader, InvocationJson.class);
    }

    @Override
    public ResponseJson readResponse(InputStream json) {
        JsonReader reader = new JsonReader(new InputStreamReader(json));
        return gson.fromJson(reader, ResponseJson.class);
    }

    /**
     * Creates type adapters for any instance of the Value interface.
     */
    private static class ValueTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType().isAssignableFrom(Value.class)) {
                return (TypeAdapter<T>) new ValueTypeAdapter(gson);
            }
            return null;
        }
    }
}
