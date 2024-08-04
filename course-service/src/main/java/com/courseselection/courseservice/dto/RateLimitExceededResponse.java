package com.courseselection.courseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RateLimitExceededResponse  {
    private String message;
    private int status;
}
