package ca.radiant3.jsonrpc.protocol.jsonrpc2;

public class InvalidProtocolVersion extends RuntimeException {
    public InvalidProtocolVersion(String version) {
        super(version);
    }
}
