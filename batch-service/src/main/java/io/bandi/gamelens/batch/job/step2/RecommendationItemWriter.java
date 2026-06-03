package io.bandi.gamelens.batch.job.step2;

import io.bandi.gamelens.batch.domain.model.Recommendation;
import io.bandi.gamelens.batch.repository.RecommendationRepository;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class RecommendationItemWriter implements ItemWriter<Recommendation> {

    private final RecommendationRepository repository;

    public RecommendationItemWriter(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(Chunk<? extends Recommendation> chunk) throws Exception {
        repository.saveAll(chunk.getItems());
    }
}
