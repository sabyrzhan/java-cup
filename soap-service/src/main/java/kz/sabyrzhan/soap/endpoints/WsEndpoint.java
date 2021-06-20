package kz.sabyrzhan.soap.endpoints;

import kz.sabyrzhan.soap.wsdl.Gender;
import kz.sabyrzhan.soap.wsdl.GetUserRequest;
import kz.sabyrzhan.soap.wsdl.GetUserResponse;
import kz.sabyrzhan.soap.wsdl.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

//@Endpoint
public class WsEndpoint {
    @PayloadRoot(
            namespace = "http://hilariousstartups.ru/soap/gen",
            localPart = "getUserRequest")
    @ResponsePayload
    @Async
    public CompletableFuture<GetUserResponse> getUserRequest(@RequestPayload GetUserRequest request) {
        return completedFuture(null).thenApply(v -> {
            User user = new User();
            user.setFirstName("Sabyrzhan");
            user.setGender(Gender.MALE);
            user.setLastName("Tynybayev");

            GetUserResponse resp = new GetUserResponse();
            resp.setUser(user);

            return resp;
        });
    }
}
