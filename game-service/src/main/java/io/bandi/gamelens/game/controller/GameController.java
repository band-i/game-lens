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
        } else if (gameService.findGame(searchedGame.id())) {
            game = gameService.getGame(searchedGame.id());
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            game = gameService.saveGame(searchedGame);
            return new ResponseEntity<>(game, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/games/{id}")
    public ResponseEntity<Game> findGame(@PathVariable("id") Long rawgId) {
        Game game = gameService.getGame(rawgId);

        if (game != null) {
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/games")
    public ResponseEntity<List<Game>> findAll() {
        List<Game> gameList = gameService.findAllGames();

        return new ResponseEntity<>(gameList, HttpStatus.OK);
    }
}
