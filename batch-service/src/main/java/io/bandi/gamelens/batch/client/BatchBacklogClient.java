package io.bandi.gamelens.batch.client;

import io.bandi.gamelens.batch.domain.dto.BacklogServiceResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * HTTP client for communication with backlog-service.
 *
 * <p>Used exclusively to verify backlog existence before backlog operations.
 */
@Component
public class BatchBacklogClient {

    private final RestClient backlogRestClient;

    public BatchBacklogClient(RestClient backlogRestClient) {
        this.backlogRestClient = backlogRestClient;
    }

    /**
     * Fetches all backlogs from backlog-service.
     *
     * @return the backlog data, or throws {@link org.springframework.web.client.RestClientException}
     * if the backlog is not found or backlog-service is unreachable
     */
    public List<BacklogServiceResponse> getResponse() {
        return backlogRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/backlog")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
