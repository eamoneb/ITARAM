DROP TABLE IF EXISTS incident;

CREATE TABLE IF NOT EXISTS incident (
    id int NOT NULL UNIQUE,
    title varchar(255) NOT NULL
);
