package com.courseselection.professorservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserProfileResponseDTO {
    private String name;
    private String email;
}
