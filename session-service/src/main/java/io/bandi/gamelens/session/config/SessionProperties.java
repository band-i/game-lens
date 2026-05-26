package io.bandi.gamelens.session.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "game-service")
public record SessionProperties(String baseUrl) {
}
