package com.courseselection.studentservice.controller;

import com.courseselection.studentservice.dtos.LoginRequestDto;
import com.courseselection.studentservice.dtos.LoginResponseDto;
import com.courseselection.studentservice.dtos.SignupRequestDto;
import com.courseselection.studentservice.model.Student;
import com.courseselection.studentservice.service.AuthenticationService;
import com.courseselection.studentservice.utility.JwtUtil;
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
    public ResponseEntity<Student> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return new ResponseEntity<>(authenticationService.signup(signupRequestDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        Student authenticatedStudent = authenticationService.authenticate(loginRequestDto);

        String jwtToken =  jwtUtil.generateToken(authenticatedStudent);

        LoginResponseDto loginResponse = LoginResponseDto
                .builder()
                .token(jwtToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .build();
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

}
