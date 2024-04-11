package com.courseselection.professorservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
public class UpdateProfessorRequestDto {
    private Optional<String> name;
}
