package com.courseselection.professorservice.dtos;

import lombok.Getter;

import java.util.Optional;

@Getter
public class UpdateProfessorRequestDto {
    private Optional<String> name;
}
