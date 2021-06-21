package kz.sabyrzhan.soap.endpoints;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RestController
@RequestMapping("/ws")
public class FakeWsEndpoint {
    Random random = new Random();
    @PostMapping
    public CompletableFuture<String> getUser(@RequestBody String body) {
        return completedFuture(null).thenApply(v -> {
            try {
                Thread.sleep(random.nextInt(5));
                String content = IOUtils.toString(getClass().getClassLoader().getResource("response.xml"), StandardCharsets.UTF_8);
                return content;
            } catch (Exception e) {
                return "";
            }
        });
    }
}
