package com.courseselection.studentservice.dtos;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class StudentResponseDto {
    private Integer id;
    private String name;
    private String email;
}
