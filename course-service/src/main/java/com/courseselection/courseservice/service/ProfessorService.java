package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Professor;
import com.courseselection.courseservice.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ProfessorService {
    private static final Logger logger = Logger.getLogger(ProfessorService.class.getName());
    @Autowired
    private ProfessorRepository professorRepository;

    public void addProfessor(com.courseselection.kafkatypes.Professor kafkaProfessor) {
        logger.info("Adding the professor: " + kafkaProfessor);
        Professor professor = Professor
                .builder()
                .id(kafkaProfessor.getId())
                .name(kafkaProfessor.getName().toString())
                .build();
        professorRepository.save(professor);
    }

    public void updateProfessor(com.courseselection.kafkatypes.Professor kafkaProfessor) {
        logger.info("Updating the professor: " + kafkaProfessor);
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
