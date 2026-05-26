CREATE TABLE game_sessions (
    id             BIGSERIAL PRIMARY KEY,
    game_id        BIGINT NOT NULL,
    started_at     TIMESTAMP DEFAULT NOW(),
    ended_at       TIMESTAMP NULL,
    notes          VARCHAR(100) NOT NULL
);