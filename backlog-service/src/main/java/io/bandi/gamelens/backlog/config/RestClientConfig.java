package io.bandi.gamelens.backlog.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(BacklogProperties.class)
public class RestClientConfig {

    private final BacklogProperties backlogProperties;

    public RestClientConfig(BacklogProperties backlogProperties) {
        this.backlogProperties = backlogProperties;
    }

    @Bean
    public RestClient backlogRestClient() {
        return RestClient.builder()
                .baseUrl(backlogProperties.baseUrl())
                .build();
    }
}
