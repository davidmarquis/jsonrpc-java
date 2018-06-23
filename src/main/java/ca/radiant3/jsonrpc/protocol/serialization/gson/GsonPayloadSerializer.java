package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Result;
import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.protocol.serialization.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

public class GsonPayloadSerializer implements PayloadSerializer {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Result.class, new ResultTypeAdapter())
            .create();


    @Override
    public void serialize(InvocationJson invocation, OutputStream out) {

    }

    @Override
    public void serialize(BatchJson batch, OutputStream out) {

    }

    @Override
    public void serialize(ResponseJson response, OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        gson.toJson(response, writer);
        writer.flush();
    }

    @Override
    public InvocationJson readInvocation(InputStream json) {
        JsonObject payload = new JsonParser().parse(new InputStreamReader(json)).getAsJsonObject();

        InvocationJson invocation = new InvocationJson()
                .withJsonrpc(payload.get("jsonrpc").getAsString())
                .withMethod(payload.get("method").getAsString());

        JsonElement params = payload.get("params");

        if (params != null) {
            if (params.isJsonArray()) {
                for (JsonElement param : params.getAsJsonArray()) {
                    invocation.addParameter(
                            new ParameterJson(new JsonValue(param)));
                }
            } else {
                JsonObject paramsObj = params.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : paramsObj.entrySet()) {
                    invocation.addParameter(
                            new ParameterJson(new JsonValue(entry.getValue()))
                                    .named(entry.getKey()));
                }
            }
        }

        return invocation;
    }

    @Override
    public BatchJson deserializeBatch(InputStream json) {
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
    }

    private class ResultTypeAdapter extends TypeAdapter<Result> {
        @Override
        public void write(JsonWriter out, Result result) throws IOException {
            if (result != null) {
                gson.toJson(result.getValue(), result.getHint(), out);
            } else {
                out.nullValue();
            }
        }

        @Override
        public Result read(JsonReader in) throws IOException {
            return null;
        }
    }
}
