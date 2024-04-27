package com.courseselection.studentservice.service;

import com.courseselection.studentservice.dtos.LoginRequestDto;
import com.courseselection.studentservice.dtos.SignupRequestDto;
import com.courseselection.studentservice.model.Student;
import com.courseselection.studentservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthenticationService {
    private static final Logger logger = Logger.getLogger(AuthenticationService.class.getName());
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public Student signup(SignupRequestDto signupRequestDto) {
        logger.info("Signup request has been made: " + signupRequestDto);
        Student student = Student
                .builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();
        return studentRepository.save(student);
    }

    public Student authenticate(LoginRequestDto input) {
        logger.info("Login request has been made: " + input);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return studentRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

}
