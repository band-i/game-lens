package io.bandi.gamelens.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch-service")
public record BatchProperties(
        String gameServiceUrl,
        String backlogServiceUrl
) {
}
