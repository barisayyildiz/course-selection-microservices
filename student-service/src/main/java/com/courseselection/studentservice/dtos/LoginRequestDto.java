package com.courseselection.studentservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotNull(message = "Email should be provided")
    private String email;
    @NotNull(message = "Password should be provided")
    private String password;
}
