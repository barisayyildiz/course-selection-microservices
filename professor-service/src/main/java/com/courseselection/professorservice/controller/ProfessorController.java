package com.courseselection.professorservice.controller;

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

@Validated
@RequestMapping("/api")
@RestController
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private ServiceUtility serviceUtility;

    @GetMapping("/professor")
    public ResponseEntity<UserProfileResponseDTO> getProfessorInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Professor currentProfessor = (Professor) authentication.getPrincipal();
        return new ResponseEntity<>(serviceUtility.hideUserDetails(currentProfessor), HttpStatus.OK);
    }

    @PutMapping("/professor")
    public ResponseEntity<UserProfileResponseDTO> updateProfessorInformation(
            @Valid @RequestBody UpdateProfessorRequestDto updateProfessorRequestDto
    ) {
        Professor professor = professorService.updateProfessor(updateProfessorRequestDto);
        return new ResponseEntity<UserProfileResponseDTO>(serviceUtility.hideUserDetails(professor), HttpStatus.OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseRequestDto>> getCourses() {
        List<CourseRequestDto> courseList = professorService.getCourses();
        return new ResponseEntity<>(courseList, HttpStatus.OK);
    }

    @PostMapping("/course")
    public ResponseEntity<CourseCreationDto> addCourse(
            @Valid @RequestBody CourseCreationDto courseCreationDto
    ) {
        professorService.createCourse(courseCreationDto);
        return new ResponseEntity<>(courseCreationDto, HttpStatus.CREATED);
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<CourseRequestDto> updateCourse(
            @PathVariable Integer id,
            @Valid @RequestBody CourseUpdateDto courseUpdateDto
    ) {
        CourseRequestDto course = professorService.updateCourse(id, courseUpdateDto);
        if(Objects.nonNull(course)) {
            return new ResponseEntity<>(course, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Integer id
    ) {
        if(professorService.deleteCourse(id)) {
            return new ResponseEntity<>("Course deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Course does not exists", HttpStatus.NOT_FOUND);
        }
    }
}

