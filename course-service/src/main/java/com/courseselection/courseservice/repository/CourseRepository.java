package com.courseselection.courseservice.repository;

import com.courseselection.courseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT DISTINCT c FROM Course c WHERE c.name ILIKE %:name%")
    List<Course> searchByName(String name);
}
