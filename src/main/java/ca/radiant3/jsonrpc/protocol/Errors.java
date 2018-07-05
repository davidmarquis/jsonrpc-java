package ca.radiant3.jsonrpc.protocol;

import ca.radiant3.jsonrpc.json.ErrorJson;

public class Errors {
    public static ErrorJson methodNotFound() {
        return ErrorJson.of(-32601)
                .withMessage("Method not found");
    }

    public static ErrorJson invalidProtocol(String version) {
        return ErrorJson.of(-32600)
                .withMessage("Invalid protocol")
                .withData("detail", String.format("Unsupported protocol version [%s]", version));
    }

    public static ErrorJson invalidParameters() {
        return ErrorJson.of(-32602)
                .withMessage("Invalid params");
    }

    public static ErrorJson invalidMimeType(String mimeType) {
        return ErrorJson.of(-32700)
                .withMessage("Invalid mime type")
                .withData("detail", String.format("Mime type should be [application/json], but was [%s]", mimeType));
    }

    public static ErrorJson invalidFormat() {
        return ErrorJson.of(-9998)
                .withMessage("Could not parse JSON");
    }
}
