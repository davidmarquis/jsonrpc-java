package ca.radiant3.jsonrpc.testkit;

import ca.radiant3.jsonrpc.protocol.InvocationPayload;

import java.io.InputStream;

public class ExamplePayload {
    private final String path;

    public ExamplePayload(String path) {
        this.path = path;
    }

    public static ExamplePayload get(String testResourcesPath) {
       return new ExamplePayload(testResourcesPath);
    }

    public InputStream read() {
        return ExamplePayload.class.getResourceAsStream(path);
    }

    public InvocationPayload asInvocation() {
        return InvocationPayload.from(read(), "application/json");
    }
}
