package com.courseselection.courseservice.controller;

import com.courseselection.courseservice.model.Course;
import com.courseselection.courseservice.repository.CourseRepository;
import com.courseselection.courseservice.repository.ProfessorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("/api/courses")
    public ResponseEntity<List<Course>> getAllCourses(HttpServletRequest request) {
        return new ResponseEntity<>(courseRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/api/courses/search")
    public ResponseEntity<List<Course>> searchByCourseName(
            @RequestParam("name") String name
    ) {
        return new ResponseEntity<>(courseRepository.searchByName(name), HttpStatus.OK);
    }

}
