CREATE TABLE groups
(
    id        SERIAL        NOT NULL,
    name      VARCHAR(255)  NOT NULL UNIQUE,
    position  INTEGER       NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE task
(
    id            SERIAL        NOT NULL,
    name          VARCHAR(255)  NOT NULL,
    description   VARCHAR(255)  NOT NULL,
    date_create   DATE          NOT NULL,
    position      INTEGER       NOT NULL,
    group_id      INTEGER       NOT NULL REFERENCES groups (id) ON DELETE CASCADE,

    UNIQUE(group_id, position),
    UNIQUE(name, description),
    PRIMARY KEY (id)
);
