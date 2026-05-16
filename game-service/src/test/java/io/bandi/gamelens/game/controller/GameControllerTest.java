package io.bandi.gamelens.game.controller;

import io.bandi.gamelens.game.domain.dto.RawgGameDto;
import io.bandi.gamelens.game.domain.dto.RawgPlatformDto;
import io.bandi.gamelens.game.domain.dto.RawgPlatformWrapper;
import io.bandi.gamelens.game.domain.model.Game;
import io.bandi.gamelens.game.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;


    private RawgGameDto buildDto(Long id, String name) {
        RawgPlatformDto platform = new RawgPlatformDto("PC");
        RawgPlatformWrapper wrapper = new RawgPlatformWrapper(platform);
        return new RawgGameDto(id, name, "http://cover.jpg", new BigDecimal("4.5"), 20, List.of(wrapper));
    }

    private Game buildGame(Long rawgId, String title) {
        Game game = new Game();
        game.setRawgId(rawgId);
        game.setTitle(title);
        game.setPlatform("PC");
        return game;
    }


    @Test
    @DisplayName("searchAndSave: returns 204 when RAWG finds nothing")
    void searchAndSave_returns204WhenNotFound() throws Exception {
        when(gameService.getInfoGame("unknown")).thenReturn(null);

        mockMvc.perform(post("/api/v1/games").param("name", "unknown"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("searchAndSave: returns 200 with game when it already exists")
    void searchAndSave_returns200WhenGameExists() throws Exception {
        RawgGameDto dto = buildDto(1L, "Phasmophobia");
        Game game = buildGame(1L, "Phasmophobia");

        when(gameService.getInfoGame("Phasmophobia")).thenReturn(dto);
        when(gameService.gameExists(1L)).thenReturn(true);
        when(gameService.getGameById(1L)).thenReturn(game);

        mockMvc.perform(post("/api/v1/games").param("name", "Phasmophobia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rawgId").value(1L))
                .andExpect(jsonPath("$.title").value("Phasmophobia"));
    }

    @Test
    @DisplayName("searchAndSave: returns 201 when game is new")
    void searchAndSave_returns201WhenGameIsNew() throws Exception {
        RawgGameDto dto = buildDto(1L, "Phasmophobia");
        Game game = buildGame(1L, "Phasmophobia");

        when(gameService.getInfoGame("Phasmophobia")).thenReturn(dto);
        when(gameService.gameExists(1L)).thenReturn(false);
        when(gameService.saveGame(dto)).thenReturn(game);

        mockMvc.perform(post("/api/v1/games").param("name", "Phasmophobia"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Phasmophobia"));
    }


    @Test
    @DisplayName("searchGame: returns 200 with game when it exists")
    void searchGame_returns200WhenFound() throws Exception {
        Game game = buildGame(1L, "Phasmophobia");
        when(gameService.getGameById(1L)).thenReturn(game);

        mockMvc.perform(get("/api/v1/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rawgId").value(1L));
    }

    @Test
    @DisplayName("searchGame: returns 404 when game does not exist")
    void searchGame_returns404WhenNotFound() throws Exception {
        when(gameService.getGameById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/games/99"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("getAllGames: returns 200 with list of games")
    void getAllGames_returns200WithList() throws Exception {
        when(gameService.getAllGames()).thenReturn(List.of(buildGame(1L, "Phasmophobia")));

        mockMvc.perform(get("/api/v1/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Phasmophobia"));
    }
}
