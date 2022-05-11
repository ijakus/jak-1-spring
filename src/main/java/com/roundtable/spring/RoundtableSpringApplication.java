package com.roundtable.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.nativex.hint.MethodHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.web.reactive.function.client.WebClient;

@TypeHint(types = String.class, methods = @MethodHint(name = "split", parameterTypes = String.class))
@SpringBootApplication
public class RoundtableSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoundtableSpringApplication.class, args);
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }
}
