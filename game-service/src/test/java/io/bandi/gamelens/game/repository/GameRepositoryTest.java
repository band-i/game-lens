package io.bandi.gamelens.game.repository;

import io.bandi.gamelens.game.config.JpaAuditingConfig;
import io.bandi.gamelens.game.domain.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:17:///gamelens_game"
})
@Import(JpaAuditingConfig.class)
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    private Game buildGame(Long rawgId, String title) {
        Game game = new Game();
        game.setRawgId(rawgId);
        game.setTitle(title);
        game.setCoverUrl("http://cover.jpg");
        game.setAveragePlaytime(20);
        game.setRating(new BigDecimal("4.5"));
        game.setPlatform("PC");
        return game;
    }

    @Test
    @DisplayName("existsByRawgId: returns true when game exists")
    void existsByRawgId_returnsTrueWhenExists() {
        gameRepository.save(buildGame(1L, "Hades"));

        assertThat(gameRepository.existsByRawgId(1L)).isTrue();
    }

    @Test
    @DisplayName("existsByRawgId: returns false when game does not exist")
    void existsByRawgId_returnsFalseWhenNotExists() {
        assertThat(gameRepository.existsByRawgId(99L)).isFalse();
    }

    @Test
    @DisplayName("findByRawgId: returns correct game")
    void findByRawgId_returnsGame() {
        gameRepository.save(buildGame(42L, "Hades"));

        Game result = gameRepository.findByRawgId(42L);

        assertThat(result).isNotNull();
        assertThat(result.getRawgId()).isEqualTo(42L);
        assertThat(result.getTitle()).isEqualTo("Hades");
    }

    @Test
    @DisplayName("findByRawgId: returns null when game does not exist")
    void findByRawgId_returnsNullWhenNotExists() {
        Game result = gameRepository.findByRawgId(99L);

        assertThat(result).isNull();
    }
}
