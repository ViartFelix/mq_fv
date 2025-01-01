-- Script for creating the tables in the db

CREATE TABLE IF NOT EXISTS player (
    uuid UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    money DECIMAL(15,2) NOT NULL DEFAULT 0
);