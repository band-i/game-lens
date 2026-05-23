package io.bandi.gamelens.backlog.repository;

import io.bandi.gamelens.backlog.config.JpaAuditingConfig;
import io.bandi.gamelens.backlog.domain.model.Backlog;
import io.bandi.gamelens.backlog.domain.model.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:17:///gamelens_backlog_items"
})
@Import(JpaAuditingConfig.class)
class BacklogRepositoryTest {

    @Autowired
    private BacklogRepository backlogRepository;

    private Backlog buildBacklog(Long gameId, Integer priority, Status status) {
        Backlog backlog = new Backlog();
        backlog.setGameId(gameId);
        backlog.setStatus(status);
        backlog.setPriority(priority);
        return backlog;
    }

    @Test
    @DisplayName("existsByGameId: returns true when backlog exists")
    void existsByGameId_returnsTrueWhenExists() {
        backlogRepository.save(buildBacklog(1L, 10, Status.PENDING));

        assertThat(backlogRepository.existsByGameId(1L)).isTrue();
    }

    @Test
    @DisplayName("existsByGameId: returns false when backlog does not exists")
    void existsByGameId_returnsFalseWhenNotExists() {
        assertThat(backlogRepository.existsByGameId(99L)).isFalse();
    }

    @Test
    @DisplayName("findByGameId: returns correct backlog")
    void findByGameId_returnsBacklog() {
        backlogRepository.save(buildBacklog(22L, 1, Status.COMPLETED));

        Backlog result = backlogRepository.findByGameId(22L);

        assertThat(result).isNotNull();
        assertThat(result.getGameId()).isEqualTo(22L);
        assertThat(result.getPriority()).isEqualTo(1);
        assertThat(result.getStatus()).isEqualTo(Status.COMPLETED);

    }

    @Test
    @DisplayName("findByGameId: returns null when backlog does not exist")
    void findByGameId_returnsNullWhenNotExists() {
        Backlog result = backlogRepository.findByGameId(99L);

        assertThat(result).isNull();
    }
}
