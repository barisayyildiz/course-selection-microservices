package com.courseselection.professorservice.dtos;

import lombok.Getter;

@Getter
public class CourseUpdateDto {
    private String name;
    private String code;
    private Integer capacity;
}
