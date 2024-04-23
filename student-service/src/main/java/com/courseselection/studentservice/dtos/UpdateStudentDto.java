package com.courseselection.studentservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentDto {
    @NotNull(message = "Student name is required")
    private String name;
}
