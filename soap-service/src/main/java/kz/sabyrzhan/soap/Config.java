package kz.sabyrzhan.soap;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Config {
    @Bean
    @Qualifier("mainExecutorService")
    public ExecutorService mainExecutorService() {
        ExecutorService service = Executors.newFixedThreadPool(4);
        return service;
    }

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(600);
    }
}
