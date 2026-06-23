package com.cursedshrine.apinexus.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${webclient.connect-timeout}")
    private int connectTimeout;

    @Value("${webclient.read-timeout}")
    private int readTimeout;

    @Value("${downstream.auth.base-url}")
    private String authBaseUrl;

    @Value("${downstream.mail.base-url}")
    private String mailBaseUrl;

    @Bean("authWebClient")
    public WebClient authWebClient() {
        return buildClient(authBaseUrl);
    }

    @Bean("mailWebClient")
    public WebClient mailWebClient() {
        return buildClient(mailBaseUrl);
    }

    private WebClient buildClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(conn -> conn.addHandlerLast(
                        new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
