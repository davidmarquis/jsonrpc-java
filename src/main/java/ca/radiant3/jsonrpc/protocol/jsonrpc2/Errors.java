package ca.radiant3.jsonrpc.protocol.jsonrpc2;

import ca.radiant3.jsonrpc.protocol.serialization.ErrorJson;

public class Errors {
    public static ErrorJson methodNotFound() {
        return ErrorJson.of(-32601)
                .withMessage("Method not found");
    }

    public static ErrorJson invalidProtocol() {
        return ErrorJson.of(-32600)
                .withMessage("Invalid protocol");
    }

    public static ErrorJson invalidParameters() {
        return ErrorJson.of(-32602)
                .withMessage("Invalid params");
    }

    public static ErrorJson invalidMimeType() {
        return ErrorJson.of(-9999)
                .withMessage("Invalid mime type");
    }

    public static ErrorJson invalidFormat() {
        return ErrorJson.of(-9998)
                .withMessage("Could not parse JSON");
    }
}
