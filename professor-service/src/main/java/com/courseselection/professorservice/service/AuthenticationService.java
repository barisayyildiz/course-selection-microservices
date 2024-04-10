package com.courseselection.professorservice.service;

import com.courseselection.professorservice.dtos.SignupRequestDto;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public Professor signup(SignupRequestDto signupRequestDto) {
        Professor professor = Professor.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();
        return professorRepository.save(professor);
    }

}
