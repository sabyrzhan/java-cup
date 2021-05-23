package kz.sabyrzhan.javacup;

import kz.sabyrzhan.javacup.models.UserPhones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RestClient {
    @Autowired
    private WebClient webClient;

    public Mono<UserPhones> getUser(int id) {
        WebClient.RequestHeadersUriSpec<?> spec = webClient.get();
        WebClient.RequestHeadersSpec<?> uri = spec.uri("/api/v1/phones/" + id);
        return Mono.defer(() -> {
            return uri.exchangeToMono(response -> {
                if (response.statusCode() == HttpStatus.OK) {
                    return response.bodyToMono(UserPhones.class);
                } else {
                    return response.createException().flatMap(Mono::error);
                }
            }).onErrorResume(throwable -> Mono.just(UserPhones.EMPTY_USER));
        });
    }
}
