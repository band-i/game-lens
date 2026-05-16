CREATE TABLE backlog_items (
    id            BIGSERIAL PRIMARY KEY,
    game_id       BIGINT NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    priority      INTEGER NOT NULL DEFAULT 0,
    updated_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);