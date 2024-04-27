package com.courseselection.courseservice.controller;

import com.courseselection.courseservice.dto.CourseResponseDto;
import com.courseselection.courseservice.dto.ResponseDto;
import com.courseselection.courseservice.model.Course;
import com.courseselection.courseservice.repository.CourseRepository;
import com.courseselection.courseservice.repository.ProfessorRepository;
import com.courseselection.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class CourseController {
    private static final Logger logger = Logger.getLogger(CourseController.class.getName());
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private CourseService courseService;

    @GetMapping("/api/courses")
    public ResponseEntity<ResponseDto<?>> getAllCourses(
            @RequestParam(value = "page") Integer pageNumber,
            @RequestParam(value = "count") Integer pageSize
    ) {
        logger.info("GET /api/courses?page=" + pageNumber + "&count=" + pageSize);
        Page<CourseResponseDto> coursePage = courseService.findAllCourses(pageNumber-1, pageSize);
        ResponseDto<?> responseDto = ResponseDto
                .builder()
                .data(coursePage.getContent())
                .page(coursePage.getPageable().getPageNumber() + 1)
                .size(coursePage.getPageable().getPageSize())
                .build();
        logger.info("GET /api/courses?page=" + pageNumber + "&count=" + pageSize + ", response: " + coursePage);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/api/courses/search")
    public ResponseEntity<List<Course>> searchByCourseName(
            @RequestParam("name") String name
    ) {
        logger.info("GET /api/courses/search?name=" + name);
        List<Course> courseList = courseRepository.searchByName(name);
        logger.info("GET /api/courses/search?name=" + name + ", response: " + courseList.toString());
        return new ResponseEntity<>(courseService.searchCoursesByName(name), HttpStatus.OK);
    }

}
