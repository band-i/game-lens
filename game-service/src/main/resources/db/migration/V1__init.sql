CREATE TABLE games (
    id                   BIGSERIAL PRIMARY KEY,
    rawg_id              BIGINT NOT NULL,
    title                VARCHAR(200) NOT NULL,
    genre                VARCHAR(100) NOT NULL,
    cover_url            VARCHAR(200),
    average_playtime     INTEGER NOT NULL,
    rating               INTEGER NOT NULL,
    platform             VARCHAR(50) NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT NOW()
);