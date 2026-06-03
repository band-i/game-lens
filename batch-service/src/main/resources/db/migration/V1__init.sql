CREATE TABLE recommendations (
    id                   BIGSERIAL PRIMARY KEY,
    game_id              BIGINT NOT NULL,
    score                INTEGER NOT NULL,
    reason               VARCHAR(200) NOT NULL,
    generated_at         TIMESTAMP
);