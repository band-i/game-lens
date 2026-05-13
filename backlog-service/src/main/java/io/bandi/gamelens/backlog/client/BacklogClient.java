package io.bandi.gamelens.backlog.client;

import io.bandi.gamelens.backlog.domain.dto.GameServiceResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BacklogClient {

    private final RestClient backlogRestClient;

    public BacklogClient(RestClient backlogRestClient) {
        this.backlogRestClient = backlogRestClient;
    }

    public GameServiceResponse getResponse(Long rawgId) {
        return backlogRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/games/{rawgId}")
                        .build(rawgId))
                .retrieve()
                .body(GameServiceResponse.class);

    }
}
