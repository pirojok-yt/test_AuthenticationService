CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username varchar(32) NOT NULL,
    password varchar(256) NOT NULL,
    role varchar(16) NOT NULL
);