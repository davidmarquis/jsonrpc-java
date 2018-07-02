package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.protocol.serialization.gson.GsonPayloadSerializer;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public abstract class BaseSerializationTest {
    @Parameterized.Parameters
    public static Collection<Object> implementations() {
        return List.of(
                new GsonPayloadSerializer()
        );
    }

    @Parameterized.Parameter
    public PayloadSerializer serializer;
}
