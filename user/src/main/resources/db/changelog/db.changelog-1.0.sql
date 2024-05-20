--liquibase formatted sql

--changeset matthew:1
CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    email    TEXT NOT NULL UNIQUE,
    age      INT NOT NULL,
    role     TEXT NOT NULL
);