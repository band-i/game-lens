package io.bandi.gamelens.backlog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "game-service")
public record BacklogProperties(String baseUrl) {
}
