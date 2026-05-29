package io.bandi.gamelens.session.controller;

import io.bandi.gamelens.session.domain.model.Session;
import io.bandi.gamelens.session.exception.GameNotFoundException;
import io.bandi.gamelens.session.exception.GlobalExceptionHandler;
import io.bandi.gamelens.session.exception.SessionEndedException;
import io.bandi.gamelens.session.exception.SessionNotFoundException;
import io.bandi.gamelens.session.service.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({SessionController.class, GlobalExceptionHandler.class})
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    private Session buildSession(Long gameId, String notes) {
        Session session = new Session();
        session.setGameId(gameId);
        session.setNotes(notes);
        return session;
    }

    @Test
    @DisplayName("saveSession: returns 404 when game does not exist")
    void saveSession_returns404WhenGameNotFound() throws Exception {
        when(sessionService.saveSession(eq(1L), any()))
                .thenThrow(new GameNotFoundException(1L));

        mockMvc.perform(post("/api/v1/session")
                        .param("rawgId", "1")
                        .param("notes", "Good Game"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("saveSession: returns 201 when session is new")
    void saveSession_returns201WhenSessionIsNew() throws Exception {
        Session session = buildSession(1L, "Good Game");

        when(sessionService.saveSession(eq(1L), isNull()))
                .thenReturn(session);

        mockMvc.perform(post("/api/v1/session").param("rawgId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.notes").value("Good Game"));
    }

    @Test
    @DisplayName("getAllSessions: returns 200 with list")
    void getAllSessions_returns200WithList() throws Exception {
        when(sessionService.getAllSessions())
                .thenReturn(List.of(buildSession(1L, "Good Game")));

        mockMvc.perform(get("/api/v1/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("getSessionById: returns 200 when found")
    void getSessionById_returns200WhenFound() throws Exception {
        Session session = buildSession(1L, "Good Game");

        when(sessionService.getSessionById(1L)).thenReturn(session);

        mockMvc.perform(get("/api/v1/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value(1L));
    }

    @Test
    @DisplayName("getSessionById: returns 404 when not found")
    void getSessionById_returns404WhenNotFound() throws Exception {
        when(sessionService.getSessionById(99L))
                .thenThrow(new SessionNotFoundException(99L));

        mockMvc.perform(get("/api/v1/session/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateSession: returns 200 with updated session")
    void updateSession_returns200WhenSessionEnded() throws Exception {
        Session updated = buildSession(1L, "Good Game");

        when(sessionService.updateSession(1L)).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes")
                        .value("Good Game"));
    }

    @Test
    @DisplayName("updateSession: returns 409 when session already Ended")
    void updateSession_returns409WhenSessionExists() throws Exception {
        when(sessionService.updateSession(1L)).thenThrow(new SessionEndedException(1L));
        mockMvc.perform(patch("/api/v1/session/1"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("updateSession: returns 404 when not found")
    void updateSession_returns404WhenNotFound() throws Exception {

        when(sessionService.updateSession(99L))
                .thenThrow(new SessionNotFoundException(99L));

        mockMvc.perform(patch("/api/v1/session/99"))
                .andExpect(status().isNotFound());
    }
}
