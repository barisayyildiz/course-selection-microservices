package com.courseselection.professorservice.controller;

import com.courseselection.professorservice.dtos.LoginRequestDto;
import com.courseselection.professorservice.dtos.LoginResponseDto;
import com.courseselection.professorservice.dtos.SignupRequestDto;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.service.AuthenticationService;
import com.courseselection.professorservice.utility.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<Professor> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return new ResponseEntity<>(authenticationService.signup(signupRequestDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        Professor authenticatedProfessor = authenticationService.authenticate(loginRequestDto);

        String jwtToken = jwtUtil.generateToken(authenticatedProfessor);

        LoginResponseDto loginResponse = LoginResponseDto
                .builder()
                .token(jwtToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
