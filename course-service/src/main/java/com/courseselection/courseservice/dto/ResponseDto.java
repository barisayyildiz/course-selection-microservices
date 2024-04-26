package com.courseselection.courseservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ResponseDto<T extends List<?>> {
    T data;
    Integer page;
    Integer size;
}
