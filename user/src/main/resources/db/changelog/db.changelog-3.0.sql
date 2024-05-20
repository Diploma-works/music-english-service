--liquibase formatted sql

--changeset matthew:1
CREATE TABLE IF NOT EXISTS track
(
    id          TEXT PRIMARY KEY,
    title       TEXT NOT NULL,
    authors     TEXT NOT NULL,
    lyric_count INTEGER
);

ALTER TABLE playlist
    ADD track_count INTEGER NOT NULL;

--changeset matthew:2
CREATE TABLE IF NOT EXISTS track_task
(
    id                 SERIAL PRIMARY KEY,
    lyric_number       INTEGER NOT NULL,
    username           TEXT    NOT NULL,
    track_id           TEXT    NOT NULL REFERENCES track (id),
    playlist_yandex_id TEXT    NOT NULL
);