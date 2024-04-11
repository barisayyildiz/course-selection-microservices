package com.courseselection.professorservice.controller;

import com.courseselection.professorservice.dtos.CourseRequestDto;
import com.courseselection.professorservice.dtos.UpdateProfessorRequestDto;
import com.courseselection.professorservice.dtos.UserProfileResponseDTO;
import com.courseselection.professorservice.model.Course;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.service.ProfessorService;
import com.courseselection.professorservice.utility.ServiceUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestBody UpdateProfessorRequestDto updateProfessorRequestDto
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
    public void addCourse() {
//        TODO: add a new course
    }

    @DeleteMapping("/course/{id}")
    public void dropCourse(@PathVariable String id) {
//        TODO: drop a course
    }

}

