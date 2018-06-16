package ca.radiant3.jsonrpc.protocol.serialization;

import ca.radiant3.jsonrpc.Invocation;
import ca.radiant3.jsonrpc.Result;

import java.io.Serializable;

public class ResponseJson {
    private String jsonrpc = "2.0";
    private Serializable id;
    private Result result;
    private ErrorJson error;

    private ResponseJson() {
    }

    public static ResponseJson to(Invocation invocation) {
        ResponseJson response = new ResponseJson();
//        response.id = invocation.id();
        return response;
    }

    public ResponseJson error(ErrorJson details) {
        this.error = details;
        return this;
    }

    public ResponseJson success(Result result) {
        this.result = result;
        return this;
    }
}
