package io.bandi.gamelens.batch.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(BatchProperties.class)
public class RestClientConfig {

    private final BatchProperties batchProperties;

    public RestClientConfig(BatchProperties backlogProperties) {
        this.batchProperties = backlogProperties;
    }

    @Bean
    public RestClient gameRestClient() {
        return RestClient.builder()
                .baseUrl(batchProperties.gameServiceUrl())
                .build();
    }

    @Bean
    public RestClient backlogRestClient() {
        return RestClient.builder()
                .baseUrl(batchProperties.backlogServiceUrl())
                .build();
    }
}
