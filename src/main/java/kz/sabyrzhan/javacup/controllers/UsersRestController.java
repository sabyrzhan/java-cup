package kz.sabyrzhan.javacup.controllers;

import kz.sabyrzhan.javacup.RestClient;
import kz.sabyrzhan.javacup.SoapClient;
import kz.sabyrzhan.javacup.TimeoutException;
import kz.sabyrzhan.javacup.models.UserPhones;
import kz.sabyrzhan.wsdl.GetUserResponse;
import kz.sabyrzhan.wsdl.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/user")
@Slf4j
public class UsersRestController {
    @Autowired
    private SoapClient soapClient;

    @Autowired
    private RestClient restClient;

    @GetMapping("/{id}")
    public Mono<UserResponse> getUserById(@PathVariable int id) {
        Mono<GetUserResponse> userDataMono = soapClient.getUser(id).subscribeOn(Schedulers.boundedElastic()).publishOn(Schedulers.boundedElastic());
        Mono<UserPhones> userPhonesMono = restClient.getUser(id).subscribeOn(Schedulers.boundedElastic()).publishOn(Schedulers.boundedElastic());

        UserResponse userResponse = new UserResponse();
        userResponse.setName("");
        userResponse.setPhone("");
        return Mono.zip(userDataMono, userPhonesMono, (userData, userPhones) -> {
            User user = userData.getUser();
            userResponse.setName(user.getFirstName() + " " + user.getLastName());
            if (userPhones != UserPhones.EMPTY_USER) {
                if (userPhones.getPhones() != null && !userPhones.getPhones().isEmpty()) {
                    userResponse.setPhone(userPhones.getPhones().get(0));
                }
            }

            return userResponse;
        }).onErrorResume(throwable -> {
            if (throwable instanceof TimeoutException) {
                userResponse.setCode(1);
            } else {
                userResponse.setCode(2);
            }

            return Mono.empty();
        }).thenReturn(userResponse);
    }
}