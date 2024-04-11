package com.courseselection.professorservice.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CourseCreationDto {
    private String name;
    private String code;
    private Integer capacity;
}
