-- Script for creating the tables in the db

CREATE TABLE IF NOT EXISTS player (
    uuid UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    money DECIMAL(15,2) NOT NULL DEFAULT 0
);