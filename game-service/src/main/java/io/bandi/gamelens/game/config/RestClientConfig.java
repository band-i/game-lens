package io.bandi.gamelens.game.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GameProperties.class)
public class RestClientConfig {

    private final GameProperties gameProperties;

    public RestClientConfig(GameProperties gameProperties) {
        this.gameProperties = gameProperties;
    }

    @Bean
    public RestClient gameRestClient() {
        return RestClient.builder()
                .baseUrl(gameProperties.baseUrl())
                .build();
    }
}
