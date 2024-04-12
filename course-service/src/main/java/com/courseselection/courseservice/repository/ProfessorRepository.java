package com.courseselection.courseservice.repository;

import com.courseselection.courseservice.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, String> {
    @Query("SELECT p FROM Professor p WHERE p.professorId = :id")
    Optional<Professor> findProfessorByProfessorId(Integer id);
}
