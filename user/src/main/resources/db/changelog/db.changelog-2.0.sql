--liquibase formatted sql

--changeset matthew:1
CREATE TABLE IF NOT EXISTS playlist
(
    id       SERIAL PRIMARY KEY,
    yandex_playlist_id TEXT NOT NULL UNIQUE,
    title TEXT NOT NULL,
    username    TEXT NOT NULL
);