package io.bandi.gamelens.batch.client;

import io.bandi.gamelens.batch.domain.dto.GameServiceResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * HTTP client for communication with game-service.
 *
 * <p>Used exclusively to verify game existence before backlog operations.
 */
@Component
public class BatchGameClient {

    private final RestClient gameRestClient;

    public BatchGameClient(RestClient gameRestClient) {
        this.gameRestClient = gameRestClient;
    }

    /**
     * Fetches a game from game-service by its RAWG ID.
     *
     * @param rawgId RAWG ID of the game
     * @return the game data, or throws {@link org.springframework.web.client.RestClientException}
     * if the game is not found or game-service is unreachable
     */
    public GameServiceResponse getResponse(Long rawgId) {
        return gameRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/games/{rawgId}")
                        .build(rawgId))
                .retrieve()
                .body(GameServiceResponse.class);

    }
}
