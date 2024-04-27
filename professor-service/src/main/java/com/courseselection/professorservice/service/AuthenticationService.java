package com.courseselection.professorservice.service;

import com.courseselection.kafkatypes.ProfessorEvent;
import com.courseselection.professorservice.controller.AuthenticationController;
import com.courseselection.professorservice.dtos.LoginRequestDto;
import com.courseselection.professorservice.dtos.SignupRequestDto;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.repository.ProfessorRepository;
import com.courseselection.professorservice.utility.Constants;
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
    private ProfessorRepository professorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private KafkaProducer kafkaProducer;

    public Professor signup(SignupRequestDto signupRequestDto) {
        logger.info("Signup request has been made: " + signupRequestDto);
        Professor professor = Professor.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .build();

        Professor savedProfessor = professorRepository.save(professor);

        com.courseselection.kafkatypes.Professor kafkaProfessor = new com.courseselection.kafkatypes.Professor();
        kafkaProfessor.setId(savedProfessor.getId());
        kafkaProfessor.setName(savedProfessor.getName());
        kafkaProfessor.setEmail(savedProfessor.getEmail());
        ProfessorEvent professorEvent = new ProfessorEvent("CREATE", kafkaProfessor);
        kafkaProducer.sendMessage(Constants.PROFESSOR_OPERATION, professorEvent);

        return savedProfessor;
    }

    public Professor authenticate(LoginRequestDto input) {
        logger.info("Login request has been made: " + input);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return professorRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

}
