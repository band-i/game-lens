package io.bandi.gamelens.game.client;

import io.bandi.gamelens.game.config.GameProperties;
import io.bandi.gamelens.game.domain.dto.RawgResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GameClient {

    private final RestClient gameRestClient;
    private final GameProperties gameProperties;

    public GameClient(RestClient gameRestClient, GameProperties gameProperties) {
        this.gameRestClient = gameRestClient;
        this.gameProperties = gameProperties;
    }

    public RawgResponse getResponse(String name) {
        return gameRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key", gameProperties.key())
                        .queryParam("search", name).build())
                .retrieve()
                .body(RawgResponse.class);

    }
}
