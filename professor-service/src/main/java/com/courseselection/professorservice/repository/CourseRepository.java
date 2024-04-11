package com.courseselection.professorservice.repository;

import com.courseselection.professorservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c WHERE c.professor.id = :professorId")
    List<Course> findByProfessorId(Integer professorId);
}
