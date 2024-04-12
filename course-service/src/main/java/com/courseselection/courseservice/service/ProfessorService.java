package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Professor;
import com.courseselection.courseservice.repository.ProfessorRepository;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public void addProfessor(GenericData.Record record) {
        Professor professor = Professor
                .builder()
                .professorId(Integer.parseInt(record.get("id").toString()))
                .name(record.get("name").toString())
                .build();
        professorRepository.save(professor);
    }

    public void updateProfessor(GenericData.Record record) {
        Integer id = Integer.parseInt(record.get("id").toString());
        Optional<Professor> optionalProfessor = professorRepository.findProfessorByProfessorId(id);
        if(optionalProfessor.isPresent()) {
            Professor persistentProfessor = optionalProfessor.get();
            if(Objects.nonNull(record.get("name"))) {
                persistentProfessor.setName(record.get("name").toString());
            }
            professorRepository.save(persistentProfessor);
        }
    }


}
