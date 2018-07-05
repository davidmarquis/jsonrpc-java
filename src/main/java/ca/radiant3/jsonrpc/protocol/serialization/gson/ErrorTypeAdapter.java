package ca.radiant3.jsonrpc.protocol.serialization.gson;

import ca.radiant3.jsonrpc.json.ErrorJson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public class ErrorTypeAdapter extends TypeAdapter<ErrorJson> {
    private final Gson gson;
    private final JsonParser parser = new JsonParser();

    public ErrorTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, ErrorJson error) throws IOException {
        out.beginObject();

        out.name("code").value(error.getCode());
        out.name("message").value(error.getMessage());

        if (!error.getData().isEmpty()) {
            out.name("data");
            gson.toJson(error.getData(), Map.class, out);
        }

        out.endObject();
        out.flush();
    }

    @Override
    public ErrorJson read(JsonReader in) throws IOException {
        JsonObject payload = parser.parse(in).getAsJsonObject();

        return ErrorJson.of(payload.get("code").getAsInt())
                        .withMessage(payload.get("message").getAsString());
    }
}
