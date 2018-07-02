package ca.radiant3.jsonrpc;

import ca.radiant3.jsonrpc.protocol.InvocationPayload;

import java.io.InputStream;

public class Example {
    private final String path;

    public Example(String path) {
        this.path = path;
    }

    public static Example get(String testResourcesPath) {
       return new Example(testResourcesPath);
    }

    public InputStream read() {
        return Example.class.getResourceAsStream(path);
    }

    public InvocationPayload asInvocation() {
        return InvocationPayload.from(read(), "application/json");
    }
}
