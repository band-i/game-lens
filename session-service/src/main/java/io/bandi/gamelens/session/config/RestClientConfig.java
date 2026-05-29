package io.bandi.gamelens.session.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(SessionProperties.class)
public class RestClientConfig {

    private final SessionProperties sessionProperties;

    public RestClientConfig(SessionProperties sessionProperties) {
        this.sessionProperties = sessionProperties;
    }

    @Bean
    public RestClient sessionRestClient() {
        return RestClient.builder()
                .baseUrl(sessionProperties.baseUrl())
                .build();
    }
}
