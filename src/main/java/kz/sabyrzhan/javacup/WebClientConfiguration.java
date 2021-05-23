package kz.sabyrzhan.javacup;

import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient webClient() throws Exception {
        final int THREADS = 500;
        final BasicThreadFactory THREADFACTORY = new BasicThreadFactory.Builder()
                .namingPattern("WebClientThread-%d")
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .build();

        final Executor EXECUTOR = new ThreadPoolExecutor(
                THREADS,
                THREADS,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                THREADFACTORY,
                new ThreadPoolExecutor.AbortPolicy());

        final NioEventLoopGroup RESOURCE= new NioEventLoopGroup(THREADS, EXECUTOR);

        final ReactorResourceFactory RESOURCE_FACTORY = new ReactorResourceFactory();
        RESOURCE_FACTORY.setLoopResources(b -> RESOURCE);
        RESOURCE_FACTORY.setUseGlobalResources(false);
        RESOURCE_FACTORY.afterPropertiesSet();

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        final int TIMEOUT = 5000;


        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .responseTimeout(Duration.ofMillis(TIMEOUT))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS)));


        final ReactorClientHttpConnector HTTP_CONNECTOR = new ReactorClientHttpConnector(RESOURCE_FACTORY, m -> httpClient);

        WebClient client = WebClient.builder()
                .baseUrl(Constants.REST_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", Constants.REST_URL))
                .clientConnector(HTTP_CONNECTOR)
                .build();

        return client;
    }
}
