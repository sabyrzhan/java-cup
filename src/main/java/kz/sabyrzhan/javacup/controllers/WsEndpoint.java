package kz.sabyrzhan.javacup.controllers;

import kz.sabyrzhan.wsdl.Gender;
import kz.sabyrzhan.wsdl.GetUserRequest;
import kz.sabyrzhan.wsdl.GetUserResponse;
import kz.sabyrzhan.wsdl.User;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class WsEndpoint {
    private static final String NAMESPACE_URI = "http://hilariousstartups.ru/soap/gen";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserRequest")
    @ResponsePayload
    public GetUserResponse getUserResponse(@RequestPayload GetUserRequest request) {
        User user = new User();
        user.setFirstName("Sabyrzhan");
        user.setLastName("Tynybayev");
        user.setGender(Gender.MALE);
        GetUserResponse response = new GetUserResponse();
        response.setUser(user);

        return response;
    }
}
