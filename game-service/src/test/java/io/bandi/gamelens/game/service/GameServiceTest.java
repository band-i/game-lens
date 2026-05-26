package io.bandi.gamelens.game.service;

import io.bandi.gamelens.game.client.GameClient;
import io.bandi.gamelens.game.domain.dto.RawgGameDto;
import io.bandi.gamelens.game.domain.dto.RawgPlatformDto;
import io.bandi.gamelens.game.domain.dto.RawgPlatformWrapper;
import io.bandi.gamelens.game.domain.dto.RawgResponse;
import io.bandi.gamelens.game.domain.model.Game;
import io.bandi.gamelens.game.exception.GameAlreadyExistsException;
import io.bandi.gamelens.game.exception.GameNotFoundException;
import io.bandi.gamelens.game.repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameClient gameClient;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;


    private RawgGameDto buildDto(Long id, String name) {
        RawgPlatformDto platform = new RawgPlatformDto("PC");
        RawgPlatformWrapper wrapper = new RawgPlatformWrapper(platform);
        return new RawgGameDto(
                id,
                name,
                "http://cover.jpg",
                new BigDecimal("4.5"),
                20,
                LocalDate.of(2005, 01, 11),
                List.of(wrapper)
        );
    }

    private Game buildGame(Long rawgId, String title) {
        Game game = new Game();
        game.setRawgId(rawgId);
        game.setTitle(title);
        return game;
    }

    @Test
    @DisplayName("getInfoGame: returns first result when RAWG responds with games")
    void getInfoGame_returnsFirstResult() {
        RawgGameDto dto = buildDto(1L, "Hades");
        when(gameClient.getResponse("Hades")).thenReturn(new RawgResponse(1, List.of(dto)));

        RawgGameDto result = gameService.getInfoGame("Hades");

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Hades");
    }

    @Test
    @DisplayName("getInfoGame: returns null when RAWG responds with empty list")
    void getInfoGame_returnsNullWhenNoResults() {
        when(gameClient.getResponse("unknown")).thenReturn(new RawgResponse(0, List.of()));

        RawgGameDto result = gameService.getInfoGame("unknown");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("saveGame: maps DTO fields to entity and persists it")
    void saveGame_mapsFieldsCorrectly() {
        RawgGameDto dto = buildDto(42L, "Hades");
        Game saved = buildGame(42L, "Hades");

        when(gameRepository.save(any(Game.class))).thenReturn(saved);
        when(gameClient.getResponse("Hades"))
                .thenReturn(new RawgResponse(1, List.of(dto)));

        Game result = gameService.saveGame("Hades");

        assertThat(result.getRawgId()).isEqualTo(42L);
        assertThat(result.getTitle()).isEqualTo("Hades");
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("saveGame: sets platform to Unknown when platforms list is empty")
    void saveGame_setsUnknownPlatformWhenListIsEmpty() {
        RawgGameDto dto = new RawgGameDto(1L, "Hades", "http://cover.jpg",
                new BigDecimal("4.5"), 20, LocalDate.now(), List.of());
        Game saved = buildGame(1L, "Hades");
        saved.setPlatform("Unknown");

        when(gameRepository.save(any(Game.class))).thenReturn(saved);
        when(gameClient.getResponse("Hades"))
                .thenReturn(new RawgResponse(1, List.of(dto)));

        Game result = gameService.saveGame("Hades");

        assertThat(result.getPlatform()).isEqualTo("Unknown");
    }


    @Test
    @DisplayName("gameExists: delegates to repository")
    void gameExists_delegatesToRepository() {
        when(gameRepository.existsByRawgId(1L)).thenReturn(true);

        assertThat(gameService.gameExists(1L)).isTrue();
        verify(gameRepository).existsByRawgId(1L);
    }

    @Test
    @DisplayName("getGameById: returns game from repository")
    void getGameById_returnsGame() {
        Game game = buildGame(1L, "Hades");
        when(gameRepository.findByRawgId(1L)).thenReturn(game);
        when(gameRepository.existsByRawgId(1L)).thenReturn(true);

        Game result = gameService.getGameById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getRawgId()).isEqualTo(1L);
    }


    @Test
    @DisplayName("getAllGames: returns all games from repository")
    void getAllGames_returnsList() {
        when(gameRepository.findAll()).thenReturn(List.of(buildGame(1L, "Hades")));

        List<Game> result = gameService.getAllGames();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("saveGame: throws GameAlreadyExistsException when game exists")
    void saveGame_throwsWhenGameAlreadyExists() {
        RawgGameDto dto = buildDto(1L, "Phasmophobia");
        RawgResponse response = new RawgResponse(1, List.of(dto));

        when(gameClient.getResponse("Phasmophobia")).thenReturn(response);
        when(gameRepository.existsByRawgId(1L)).thenReturn(true);

        assertThatThrownBy(() -> gameService.saveGame("Phasmophobia"))
                .isInstanceOf(GameAlreadyExistsException.class);
    }

    @Test
    @DisplayName("saveGame: throws GameNotFoundException when RAWG returns no results")
    void saveGame_throwsWhenGameNotFoundInRawg() {
        when(gameClient.getResponse("unknown")).thenReturn(new RawgResponse(0, List.of()));

        assertThatThrownBy(() -> gameService.saveGame("unknown"))
                .isInstanceOf(GameNotFoundException.class);
    }
}
