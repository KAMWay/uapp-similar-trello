CREATE TABLE groups
(
    id        SERIAL        NOT NULL,
    name      VARCHAR(255)  NOT NULL UNIQUE,
    position  INTEGER       NOT NULL UNIQUE,

    PRIMARY KEY (id)
);
