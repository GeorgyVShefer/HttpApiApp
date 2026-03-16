CREATE TABLE short_urls
(
    id           BIGSERIAL PRIMARY KEY,
    short_code   VARCHAR(16)   NOT NULL UNIQUE,
    original_url VARCHAR(2048) NOT NULL,
    created_at   TIMESTAMP     NOT NULL
);