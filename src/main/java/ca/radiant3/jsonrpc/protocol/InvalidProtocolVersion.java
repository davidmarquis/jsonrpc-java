package ca.radiant3.jsonrpc.protocol;

public class InvalidProtocolVersion extends RuntimeException {
    public InvalidProtocolVersion(String version) {
        super(version);
    }
}
