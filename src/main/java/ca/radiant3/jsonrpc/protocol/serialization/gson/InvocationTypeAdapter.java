package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.protocol.payload.InvocationJson;
import ca.radiant3.jsonrpc.protocol.payload.ParametersJson;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvocationTypeAdapter extends TypeAdapter<InvocationJson> {
    private final Gson gson;
    private final JsonParser parser = new JsonParser();

    public InvocationTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, InvocationJson invocation) throws IOException {
        out.beginObject();

        out.name("jsonrpc").value(invocation.getJsonrpc());

        out.name("method").value(invocation.getMethod());

        if (invocation.hasParameters()) {
            out.name("params");
            ParametersJson parameters = invocation.getParams();

            if (parameters.hasNamedParameters()) {
                out.beginObject();
                Map<String, Value> values = parameters.toMap();
                for (Map.Entry<String, Value> entry : values.entrySet()) {
                    out.name(entry.getKey());
                    gson.toJson(entry.getValue(), Value.class, out);
                }
                out.endObject();
            } else {
                out.beginArray();
                parameters.values().forEach(v -> gson.toJson(v, Value.class, out));
                out.endArray();
            }
        }

        out.endObject();
        out.flush();
    }

    @Override
    public InvocationJson read(JsonReader in) {
        JsonObject payload = parser.parse(in).getAsJsonObject();

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
                    paramValues.add(new JsonElementValue(gson, param));
                }
                parameters = ParametersJson.unnamed(paramValues);

            } else {
                parameters = ParametersJson.named();

                JsonObject paramsObj = params.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : paramsObj.entrySet()) {
                    parameters.add(entry.getKey(), new JsonElementValue(gson, entry.getValue()));
                }
            }
            invocation.withParameters(parameters);
        }

        return invocation;
    }
}
