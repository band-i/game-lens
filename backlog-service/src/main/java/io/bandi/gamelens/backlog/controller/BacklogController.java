package io.bandi.gamelens.backlog.controller;

import io.bandi.gamelens.backlog.domain.dto.BacklogRequest;
import io.bandi.gamelens.backlog.domain.model.Backlog;
import io.bandi.gamelens.backlog.service.BacklogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BacklogController {

    private final BacklogService backlogService;

    public BacklogController(BacklogService backlogService) {
        this.backlogService = backlogService;
    }

    @PostMapping(value = "/backlog")
    public ResponseEntity<Backlog> saveBacklog(
            @RequestParam Long gameId,
            @RequestParam(required = false) Integer priority
    ) {
        if (backlogService.gameExists(gameId)) {
            if (backlogService.backlogExists(gameId)) {
                return new ResponseEntity<>(backlogService.getBacklogById(gameId), HttpStatus.OK);
            } else {
                Backlog backlog = backlogService.saveBacklog(gameId, priority);
                return new ResponseEntity<>(backlog, HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/backlog")
    public ResponseEntity<List<Backlog>> getAllBacklogs() {
        return new ResponseEntity<>(backlogService.getAllBacklogs(), HttpStatus.OK);

    }

    @GetMapping(value = "/backlog/{gameId}")
    public ResponseEntity<Backlog> getBacklogById(@PathVariable Long gameId) {

        if (backlogService.backlogExists(gameId)) {
            return new ResponseEntity<>(backlogService.getBacklogById(gameId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PatchMapping(value = "/backlog/{gameId}")
    public ResponseEntity<Backlog> updateBacklog(
            @PathVariable Long gameId,
            @RequestBody BacklogRequest backlogRequest
    ) {
        if (backlogService.backlogExists(gameId)) {
            return new ResponseEntity<>(backlogService.updateBacklog(gameId, backlogRequest), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping(value = "/backlog/{id}")
    public ResponseEntity<Void> deleteBacklog(@PathVariable Long id) {
        backlogService.deleteBacklog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
