package io.bandi.gamelens.batch.job.step2;

import io.bandi.gamelens.batch.client.BatchBacklogClient;
import io.bandi.gamelens.batch.domain.dto.BacklogServiceResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemReader;

import java.util.Iterator;

/**
 * Reads all backlog entries from backlog-service.
 *
 * <p>Fetches the full list on the first {@link #read()} call and iterates
 * over it one item at a time. Returns {@code null} when the list is exhausted,
 * signaling Spring Batch to end the step.
 */
public class RecommendationItemReader implements ItemReader<BacklogServiceResponse> {

    private final BatchBacklogClient batchBacklogClient;

    private Iterator<BacklogServiceResponse> backlogResponseIterator = null;

    public RecommendationItemReader(BatchBacklogClient batchBacklogClient) {
        this.batchBacklogClient = batchBacklogClient;
    }

    @Override
    public @Nullable BacklogServiceResponse read() {
        if (backlogResponseIterator == null) {
            backlogResponseIterator = batchBacklogClient.getResponse().iterator();
        }
        if (backlogResponseIterator.hasNext()) {
            return backlogResponseIterator.next();
        }
        return null;
    }
}
