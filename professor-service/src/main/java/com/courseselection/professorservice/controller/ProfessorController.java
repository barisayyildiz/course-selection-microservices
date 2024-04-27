package com.courseselection.professorservice.controller;

import com.courseselection.professorservice.configuration.JwtAuthenticationFilter;
import com.courseselection.professorservice.dtos.*;
import com.courseselection.professorservice.model.Course;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.service.ProfessorService;
import com.courseselection.professorservice.utility.ServiceUtility;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Validated
@RequestMapping("/api")
@RestController
public class ProfessorController {
    private static final Logger logger = Logger.getLogger(ProfessorController.class.getName());
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private ServiceUtility serviceUtility;

    @GetMapping("/professor")
    public ResponseEntity<UserProfileResponseDTO> getProfessorInformation() {
        logger.info("GET /api/professor");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Professor currentProfessor = (Professor) authentication.getPrincipal();
        return new ResponseEntity<>(serviceUtility.hideUserDetails(currentProfessor), HttpStatus.OK);
    }

    @PutMapping("/professor")
    public ResponseEntity<UserProfileResponseDTO> updateProfessorInformation(
            @Valid @RequestBody UpdateProfessorRequestDto updateProfessorRequestDto
    ) {
        logger.info("PUT /api/professor");
        Professor professor = professorService.updateProfessor(updateProfessorRequestDto);
        return new ResponseEntity<UserProfileResponseDTO>(serviceUtility.hideUserDetails(professor), HttpStatus.OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseRequestDto>> getCourses() {
        logger.info("GET /api/courses");
        List<CourseRequestDto> courseList = professorService.getCourses();
        return new ResponseEntity<>(courseList, HttpStatus.OK);
    }

    @PostMapping("/course")
    public ResponseEntity<CourseCreationDto> addCourse(
            @Valid @RequestBody CourseCreationDto courseCreationDto
    ) {
        logger.info("POST /api/course");
        professorService.createCourse(courseCreationDto);
        return new ResponseEntity<>(courseCreationDto, HttpStatus.CREATED);
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<CourseRequestDto> updateCourse(
            @PathVariable Integer id,
            @Valid @RequestBody CourseUpdateDto courseUpdateDto
    ) {
        logger.info("PUT /api/course/" + id);
        CourseRequestDto course = professorService.updateCourse(id, courseUpdateDto);
        if(Objects.nonNull(course)) {
            logger.info("Course updated");
            return new ResponseEntity<>(course, HttpStatus.OK);
        } else {
            logger.info("Course not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Integer id
    ) {
        logger.info("DELETE /api/course/" + id);
        if(professorService.deleteCourse(id)) {
            logger.info("Course deleted");
            return new ResponseEntity<>("Course deleted", HttpStatus.OK);
        } else {
            logger.info("Course does not exist");
            return new ResponseEntity<>("Course does not exist", HttpStatus.NOT_FOUND);
        }
    }
}

