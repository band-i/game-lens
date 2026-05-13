package io.bandi.gamelens.game.controller;

import io.bandi.gamelens.game.domain.dto.RawgGameDto;
import io.bandi.gamelens.game.domain.model.Game;
import io.bandi.gamelens.game.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(value = "/games")
    public ResponseEntity<Game> searchAndSave(@RequestParam String name) {
        RawgGameDto searchedGame = gameService.getInfoGame(name);
        Game game;

        if (searchedGame == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (gameService.gameExists(searchedGame.id())) {
            game = gameService.getGameById(searchedGame.id());
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            game = gameService.saveGame(searchedGame);
            return new ResponseEntity<>(game, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/games/{rawgId}")
    public ResponseEntity<Game> getGameById(@PathVariable("rawgId") Long rawgId) {
        Game game = gameService.getGameById(rawgId);

        if (game != null) {
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/games")
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> gameList = gameService.getAllGames();

        return new ResponseEntity<>(gameList, HttpStatus.OK);
    }
}
