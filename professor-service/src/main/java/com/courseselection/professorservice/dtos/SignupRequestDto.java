package com.courseselection.professorservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Password required")
    private String password;
}
