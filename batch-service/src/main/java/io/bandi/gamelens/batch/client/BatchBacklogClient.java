package io.bandi.gamelens.batch.client;

import io.bandi.gamelens.batch.domain.dto.BacklogServiceResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * HTTP client for communication with backlog-service.
 *
 * <p>Used to fetch all backlog entries for processing by the batch job.
 */
@Component
public class BatchBacklogClient {

    private final RestClient backlogRestClient;

    public BatchBacklogClient(RestClient backlogRestClient) {
        this.backlogRestClient = backlogRestClient;
    }

    /**
     * Fetches all backlog entries from backlog-service for processing by the batch job.
     *
     * @return list of backlog entries
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
