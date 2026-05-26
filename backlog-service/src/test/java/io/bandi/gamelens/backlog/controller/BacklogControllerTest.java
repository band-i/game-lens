package io.bandi.gamelens.backlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bandi.gamelens.backlog.domain.dto.BacklogRequest;
import io.bandi.gamelens.backlog.domain.model.Backlog;
import io.bandi.gamelens.backlog.domain.model.Status;
import io.bandi.gamelens.backlog.exception.BacklogNotFoundException;
import io.bandi.gamelens.backlog.exception.GameAlreadyInBacklogException;
import io.bandi.gamelens.backlog.exception.GameNotFoundException;
import io.bandi.gamelens.backlog.exception.GlobalExceptionHandler;
import io.bandi.gamelens.backlog.service.BacklogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BacklogController.class, GlobalExceptionHandler.class})
class BacklogControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BacklogService backlogService;

    private Backlog buildBacklog(Long gameId, Status status) {
        Backlog backlog = new Backlog();
        backlog.setGameId(gameId);
        backlog.setStatus(status);
        backlog.setPriority(1);
        return backlog;
    }

    @Test
    @DisplayName("saveBacklog: returns 404 when game does not exist")
    void saveBacklog_returns404WhenGameNotFound() throws Exception {
        when(backlogService.saveBacklog(eq(1L), any()))
                .thenThrow(new GameNotFoundException(1L));

        mockMvc.perform(post("/api/v1/backlog")
                        .param("gameId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("saveBacklog: returns 409 when backlog already exists")
    void saveBacklog_returns200WhenBacklogExists() throws Exception {
        when(backlogService.saveBacklog(1L, 2))
                .thenThrow(new GameAlreadyInBacklogException(1L));

        mockMvc.perform(post("/api/v1/backlog")
                        .param("gameId", "1")
                        .param("priority", "2"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("saveBacklog: returns 201 when backlog is new")
    void saveBacklog_returns201WhenBacklogIsNew() throws Exception {
        Backlog backlog = buildBacklog(1L, Status.PENDING);

        when(backlogService.saveBacklog(eq(1L), isNull())).thenReturn(backlog);

        mockMvc.perform(post("/api/v1/backlog").param("gameId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("getAllBacklogs: returns 200 with list")
    void getAllBacklogs_returns200WithList() throws Exception {
        when(backlogService.getAllBacklogs()).thenReturn(List.of(buildBacklog(1L, Status.PENDING)));

        mockMvc.perform(get("/api/v1/backlog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("getBacklogById: returns 200 when found")
    void getBacklogById_returns200WhenFound() throws Exception {
        Backlog backlog = buildBacklog(1L, Status.PENDING);

        when(backlogService.getBacklogById(1L)).thenReturn(backlog);

        mockMvc.perform(get("/api/v1/backlog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value(1L));
    }

    @Test
    @DisplayName("getBacklogById: returns 404 when not found")
    void getBacklogById_returns404WhenNotFound() throws Exception {
        when(backlogService.getBacklogById(99L))
                .thenThrow(new BacklogNotFoundException(99L));

        mockMvc.perform(get("/api/v1/backlog/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateBacklog: returns 200 with updated backlog")
    void updateBacklog_returns200WhenFound() throws Exception {
        Backlog updated = buildBacklog(1L, Status.IN_PROGRESS);
        BacklogRequest request = new BacklogRequest(Status.IN_PROGRESS, 1);

        when(backlogService.updateBacklog(1L, request)).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/backlog/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("updateBacklog: returns 404 when not found")
    void updateBacklog_returns404WhenNotFound() throws Exception {
        BacklogRequest request = new BacklogRequest(Status.IN_PROGRESS, 1);

        when(backlogService.updateBacklog(eq(99L), any(BacklogRequest.class)))
                .thenThrow(new BacklogNotFoundException(99L));

        mockMvc.perform(patch("/api/v1/backlog/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deleteBacklog: returns 204")
    void deleteBacklog_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/backlog/1"))
                .andExpect(status().isNoContent());

        verify(backlogService, times(1)).deleteBacklog(1L);
    }
}
