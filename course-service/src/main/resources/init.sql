CREATE TABLE professors (
    id SERIAL PRIMARY KEY,
    professorId INTEGER, -- used inside professor microservice
    name VARCHAR(255) NOT NULL
);

CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(12) NOT NULL,
    professorId INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    enrolled INTEGER CHECK (enrolled >= 0) NOT NULL,
    FOREIGN KEY (professorId) REFERENCES professors(id)
);
INSERT INTO professors (name)
VALUES ('Hasari Çelebi'),
       ('İbrahim Soğukpınar'),
       ('Didem Gözüpek'),
       ('Erkan Zergeroğlu'),
       ('Mehmet Göktürk'),
       ('Habil Kalkan'),
       ('Tülay Ayyıldız'),
       ('Alp Arslan Bayrakçı'),
       ('Yakup Genç');

insert into courses (name, professorid, capacity, enrolled, code)
values
    ('Introduction to Computer Engineering', 1, 200, 0, 'CSE101'),
    ('Computer Programming', 2, 200, 0, 'CSE102'),
    ('Object Oriented Programming', 3, 150, 0, 'CSE241'),
    ('Discrete Mathematics', 4, 120, 0, 'CSE211'),
    ('Data Structures And Algorithms', 5, 170, 0, 'CSE222'),
    ('Logic Circuits And Design', 6, 100, 0, 'CSE232'),
    ('Software Engineering', 7, 140, 0, 'CSE343'),
    ('Programming Languages', 8, 130, 0, 'CSE341'),
    ('Computer Organizations', 9, 90, 0, 'CSE331'),
    ('Introduction To Algorithm Design', 1, 110, 0, 'CSE321'),
    ('Computer Engineering Project', 2, 180, 0, 'CSE396'),
    ('Operating Systems', 3, 130, 0, 'CSE312'),
    ('Probability and Statistics', 4, 70, 0, 'MATH118'),
    ('System Programming', 5, 105, 0, 'CSE344'),
    ('Signals and Systems', 6, 60, 0, 'CSE351');


