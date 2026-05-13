package io.bandi.gamelens.game.service;

import io.bandi.gamelens.game.client.GameClient;
import io.bandi.gamelens.game.domain.dto.RawgGameDto;
import io.bandi.gamelens.game.domain.model.Game;
import io.bandi.gamelens.game.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {

    private final GameClient gameClient;
    private final GameRepository gameRepository;

    public GameService(GameClient gameClient, GameRepository gameRepository) {
        this.gameClient = gameClient;
        this.gameRepository = gameRepository;
    }

    public Game saveGame(RawgGameDto rawgGameDto) {
        Game game = new Game();
        game.setRawgId(rawgGameDto.id());
        game.setTitle(rawgGameDto.name());
        game.setCoverUrl(rawgGameDto.backgroundImage());
        game.setAveragePlaytime(rawgGameDto.playtime());
        game.setRating(rawgGameDto.rating());
        game.setPlatform(rawgGameDto.platforms().getFirst().platform().name());
        game.setCreatedAt(LocalDateTime.now());
        return gameRepository.save(game);
    }

    public RawgGameDto getInfoGame(String name) {
        List<RawgGameDto> results = gameClient.getResponse(name).results();

        return results.stream()
                .findFirst()
                .orElse(null);
    }

    public boolean findGame(Long rawgId) {
        return gameRepository.existsByRawgId(rawgId);
    }

    public Game getGame(Long rawgId) {
        return gameRepository.findByRawgId(rawgId);
    }

    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }
}
