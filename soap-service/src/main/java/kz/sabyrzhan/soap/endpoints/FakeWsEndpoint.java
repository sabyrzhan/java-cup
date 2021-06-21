package kz.sabyrzhan.soap.endpoints;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RestController
@RequestMapping("/ws")
public class FakeWsEndpoint {
    Random random = new Random();

    @Autowired
    @Qualifier("mainExecutorService")
    private ExecutorService mainExecutorService;

    @Autowired
    private RateLimiter rateLimiter;

    @PostMapping
    public CompletableFuture<String> getUser(@RequestBody String body) {
        rateLimiter.acquire();
        return CompletableFuture.supplyAsync(() -> {
            try {
//                Thread.sleep(random.nextInt(5));
                String content = IOUtils.toString(getClass().getClassLoader().getResource("response.xml"), StandardCharsets.UTF_8);
                return content;
            } catch (Exception e) {
                return "";
            }
        }, mainExecutorService);
    }
}
