package com.courseselection.professorservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CourseCreationDto {
    @NotNull(message = "Course name is required")
    private String name;

    @NotNull(message = "Course code is required")
    private String code;

    @NotNull(message = "Course capacity is required")
    private Integer capacity;
}
