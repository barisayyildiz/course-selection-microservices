package com.courseselection.professorservice.controller;

import com.courseselection.professorservice.dtos.SignupRequestDto;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ProfessorController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Professor> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return new ResponseEntity<>(authenticationService.signup(signupRequestDto), HttpStatus.OK);
    }

}
