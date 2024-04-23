package com.courseselection.professorservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateProfessorRequestDto {
    @NotNull(message = "Professor name required")
    private String name;
}
