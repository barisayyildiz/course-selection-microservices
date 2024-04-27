package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Professor;
import com.courseselection.courseservice.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public void addProfessor(com.courseselection.kafkatypes.Professor kafkaProfessor) {
        Professor professor = Professor
                .builder()
                .id(kafkaProfessor.getId())
                .name(kafkaProfessor.getName().toString())
                .build();
        professorRepository.save(professor);
    }

    public void updateProfessor(com.courseselection.kafkatypes.Professor kafkaProfessor) {
        Integer id = kafkaProfessor.getId();
        Optional<Professor> optionalProfessor = professorRepository.findById(id);
        if(optionalProfessor.isPresent()) {
            Professor persistentProfessor = optionalProfessor.get();
            if(Objects.nonNull(kafkaProfessor.getName())) {
                persistentProfessor.setName(kafkaProfessor.getName().toString());
            }
            professorRepository.save(persistentProfessor);
        }
    }


}
