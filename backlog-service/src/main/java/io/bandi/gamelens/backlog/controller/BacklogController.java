package io.bandi.gamelens.backlog.controller;

import io.bandi.gamelens.backlog.domain.dto.BacklogRequest;
import io.bandi.gamelens.backlog.domain.model.Backlog;
import io.bandi.gamelens.backlog.service.BacklogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/backlog")
public class BacklogController {

    private final BacklogService backlogService;

    public BacklogController(BacklogService backlogService) {
        this.backlogService = backlogService;
    }

    @PostMapping
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

    @GetMapping
    public ResponseEntity<List<Backlog>> getAllBacklogs() {
        return new ResponseEntity<>(backlogService.getAllBacklogs(), HttpStatus.OK);

    }

    @GetMapping(value = "/{gameId}")
    public ResponseEntity<Backlog> getBacklogById(@PathVariable Long gameId) {

        if (backlogService.backlogExists(gameId)) {
            return new ResponseEntity<>(backlogService.getBacklogById(gameId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{gameId}")
    public ResponseEntity<Backlog> updateBacklog(
            @PathVariable Long gameId,
            @RequestBody BacklogRequest backlogRequest
    ) {
        if (backlogService.backlogExists(gameId)) {
            return new ResponseEntity<>(backlogService.updateBacklog(gameId, backlogRequest), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBacklog(@PathVariable Long id) {
        backlogService.deleteBacklog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
