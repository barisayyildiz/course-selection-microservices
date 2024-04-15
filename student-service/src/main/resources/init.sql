CREATE TABLE students (
    id Serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE enrolled_courses (
    courseId INTEGER NOT NULL,
    studentId INTEGER NOT NULL,
    PRIMARY KEY (courseId, studentId),
    FOREIGN KEY (studentId) REFERENCES students(id)
);
