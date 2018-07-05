package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.json.ErrorJson;
import ca.radiant3.jsonrpc.json.ResponseJson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

public class ResponseTypeAdapter extends TypeAdapter<ResponseJson> {
    private final Gson gson;
    private final JsonParser parser = new JsonParser();

    public ResponseTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, ResponseJson response) throws IOException {
        out.beginObject()
            .name("jsonrpc").value(response.getJsonrpc());

        Serializable id = response.getId();
        out.name("id");
        if (id != null) {
            // todo: use Value abstraction instead for id?
            gson.toJson(id, id.getClass(), out);
        } else {
            out.nullValue();
        }

        Value result = response.getResult();
        if (result != null) {
            out.name("result");
            gson.toJson(result, Value.class, out);
        }

        ErrorJson error = response.getError();
        if (error != null) {
            out.name("error");
            gson.toJson(error, ErrorJson.class, out);
        }

        out.endObject();
        out.flush();
    }

    @Override
    public ResponseJson read(JsonReader in) throws IOException {
        JsonObject payload = parser.parse(in).getAsJsonObject();

        ResponseJson response = ResponseJson.version(payload.get("jsonrpc").getAsString());

        Optional.ofNullable(payload.get("id"))
                .map(id -> id.isJsonNull() ? null : id.getAsInt())
                .ifPresent(response::withId);

        Optional.ofNullable(payload.get("result"))
                .map(result -> new JsonValue(gson, result))
                .ifPresent(response::success);

        Optional.ofNullable(payload.get("error"))
                .map(error -> gson.fromJson(error, ErrorJson.class))
                .ifPresent(response::error);

        return response;
    }
}
