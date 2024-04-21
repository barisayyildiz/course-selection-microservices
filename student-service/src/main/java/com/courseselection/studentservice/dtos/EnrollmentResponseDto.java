package com.courseselection.studentservice.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class EnrollmentResponseDto {
    private String message;
}
