package com.courseselection.studentservice.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
}
