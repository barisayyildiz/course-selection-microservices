package com.courseselection.professorservice.dtos;

import lombok.Getter;

import java.util.Optional;

@Getter
public class CourseUpdateDto {
    private Optional<String> name;
    private Optional<String> code;
    private Optional<Integer> capacity;
}
