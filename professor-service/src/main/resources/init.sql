CREATE TABLE professors (
    id Serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(12) NOT NULL,
    professorId INTEGER NOT NULL,
    FOREIGN KEY (professorId) REFERENCES professors(id)
);
