package io.bandi.gamelens.game.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rawg.api")
public record GameProperties(String key, String baseUrl) {
}
