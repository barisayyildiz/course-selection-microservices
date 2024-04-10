package com.courseselection.professorservice.service;

import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfessorService {
    @Autowired
    ProfessorRepository professorRepository;

    public Optional<Professor> getProfessorById(Integer id) {
        return professorRepository.findById(id);
    }
}
