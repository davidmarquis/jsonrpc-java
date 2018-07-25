package ca.radiant3.jsonrpc.client;

import ca.radiant3.jsonrpc.Value;
import ca.radiant3.jsonrpc.json.InvocationJson;
import ca.radiant3.jsonrpc.json.ResponseJson;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static ca.radiant3.jsonrpc.testkit.InvocationJsonThat.hasSameState;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class HttpRpcClientProxyTest {

    RemoteRpcServiceFake remoteServiceFake = new RemoteRpcServiceFake();

    MyService proxy = RpcClientProxy.createFor(MyService.class, remoteServiceFake);

    @Test
    public void generatesRemoteInvocation() {
        remoteServiceFake.willRespond(ResponseJson.toNotification().success(Value.of("my response")));

        String response = proxy.remoteMethod("test", 123);

        assertThat(response, equalTo("my response"));
        assertThat(remoteServiceFake.lastInvocation, hasSameState(InvocationJson.of("remoteMethod")
                                                                                .withParameters(Value.of("test"), Value.of(123))));
    }

    class RemoteRpcServiceFake implements RemoteRpcService {
        ResponseJson response;
        InvocationJson lastInvocation;

        public void willRespond(ResponseJson response) {
            this.response = response;
        }

        @Override
        public CompletableFuture<ResponseJson> invoke(InvocationJson invocation) throws IOException {
            this.lastInvocation = invocation;
            return CompletableFuture.completedFuture(response);
        }
    }

    interface MyService {
        String remoteMethod(String param1, int param2);
    }
}