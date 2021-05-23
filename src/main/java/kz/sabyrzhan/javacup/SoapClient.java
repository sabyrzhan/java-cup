package kz.sabyrzhan.javacup;

import kz.sabyrzhan.wsdl.GetUserRequest;
import kz.sabyrzhan.wsdl.GetUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import reactor.core.publisher.Mono;

import java.net.SocketTimeoutException;

@Slf4j
public class SoapClient extends WebServiceGatewaySupport {
    public Mono<GetUserResponse> getUser(int userId) {

        GetUserRequest request = new GetUserRequest();
        request.setUserId(userId);

        return Mono.defer(() -> {
            try {
                GetUserResponse response = (GetUserResponse) getWebServiceTemplate()
                        .marshalSendAndReceive(Constants.WS_URL, request, new SoapActionCallback("getUserRequest"));
                return Mono.just(response);
            } catch (Exception e) {
                log.error("Error", e);
                if (e.getCause() instanceof SocketTimeoutException) {
                    return Mono.error(new TimeoutException());
                } else {
                    return Mono.error(new ServerException());
                }
            }
        });
    }
}
