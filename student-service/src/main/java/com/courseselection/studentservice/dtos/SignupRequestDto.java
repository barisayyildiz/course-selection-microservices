package com.courseselection.studentservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class SignupRequestDto {
    @NotNull(message = "Name should be provided")
    private String name;
    @NotNull(message = "Email should be provided")
    private String email;
    @NotNull(message = "Password should be provided")
    private String password;
}
