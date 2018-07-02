package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.protocol.serialization.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GsonPayloadSerializer implements PayloadSerializer {
    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Value.class, new ValueTypeAdapter())
            .create();


    @Override
    public void serialize(InvocationJson invocation, OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);

        JsonWriter json = new JsonWriter(writer);
        json.beginObject()
            .name("jsonrpc").value(invocation.getJsonrpc());

        json.name("method").value(invocation.getMethod());

        if (invocation.hasParameters()) {
            json.name("params");
            ParametersJson parameters = invocation.getParams();

            if (parameters.hasNamedParameters()) {
                json.beginObject();
                Map<String, Value> parametersMap = parameters.toMap();
                for (Map.Entry<String, Value> entry : parametersMap.entrySet()) {
                    json.name(entry.getKey());
                    writeValue(entry.getValue(), json);
                }
                json.endObject();
            } else {
                json.beginArray();
                parameters.values().forEach(value -> writeValue(value, json));
                json.endArray();
            }
        }

        json.endObject();
        json.flush();
    }

    @Override
    public void serialize(BatchJson batch, OutputStream out) {

    }

    @Override
    public void serialize(ResponseJson response, OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);

        JsonWriter json = new JsonWriter(writer);
        json.beginObject()
            .name("jsonrpc").value(response.getJsonrpc());

        Serializable id = response.getId();
        json.name("id");
        if (id != null) {
            // todo: use Value abstraction instead for id?
            gson.toJson(id, id.getClass(), json);
        } else {
            json.nullValue();
        }

        Value result = response.getResult();
        if (result != null) {
            json.name("result");
            writeValue(result, json);
        }

        ErrorJson error = response.getError();
        if (error != null) {
            json.name("error");
            gson.toJson(error, ErrorJson.class, json);
        }

        json.endObject();
        json.flush();
    }

    private void writeValue(Value value, JsonWriter json) {
        json.setSerializeNulls(true);
        gson.toJson(value.get(), value.type(), json);
    }

    @Override
    public InvocationJson readInvocation(InputStream json) {
        JsonObject payload = new JsonParser().parse(new InputStreamReader(json)).getAsJsonObject();

        InvocationJson invocation = new InvocationJson()
                .withJsonrpc(payload.get("jsonrpc").getAsString())
                .withMethod(payload.get("method").getAsString());

        JsonElement params = payload.get("params");

        if (params != null) {
            ParametersJson parameters;
            if (params.isJsonArray()) {
                JsonArray paramsArray = params.getAsJsonArray();
                List<Value> paramValues = new ArrayList<>(paramsArray.size());

                for (JsonElement param : paramsArray) {
                    paramValues.add(new JsonValue(param));
                }
                parameters = ParametersJson.of(paramValues);

            } else {
                parameters = ParametersJson.named();

                JsonObject paramsObj = params.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : paramsObj.entrySet()) {
                    parameters.add(entry.getKey(), new JsonValue(entry.getValue()));
                }
            }
            invocation.withParameters(parameters);
        }

        return invocation;
    }

    @Override
    public ResponseJson readResponse(InputStream json) {
        JsonObject payload = new JsonParser().parse(new InputStreamReader(json)).getAsJsonObject();

        ResponseJson response = ResponseJson.version(payload.get("jsonrpc").getAsString());

        Optional.ofNullable(payload.get("id"))
                .map(id -> id.isJsonNull() ? null : id.getAsInt())
                .ifPresent(response::withId);

        Optional.ofNullable(payload.get("result"))
                .map(JsonValue::new)
                .ifPresent(response::success);

        Optional.ofNullable(payload.get("error"))
                .map(error -> gson.fromJson(error, ErrorJson.class))
                .ifPresent(response::error);

        return response;
    }

    @Override
    public BatchJson readBatch(InputStream json) {
        return null;
    }

    private class JsonValue implements Value {
        private JsonElement element;

        private JsonValue(JsonElement element) {
            this.element = element;
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

    private class ValueTypeAdapter extends TypeAdapter<Value> {
        @Override
        public void write(JsonWriter out, Value value) throws IOException {
            writeValue(value, out);
        }

        @Override
        public Value read(JsonReader in) throws IOException {
            return null;
        }
    }
}
