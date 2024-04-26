package com.courseselection.courseservice.repository;

import com.courseselection.courseservice.dto.CourseResponseDto;
import com.courseselection.courseservice.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, PagingAndSortingRepository<Course, Integer> {
    @Query("SELECT DISTINCT c FROM Course c WHERE c.name ILIKE %:name%")
    List<Course> searchByName(String name);

    @Query("SELECT c.id AS id, c.name AS name, c.code AS code, c.capacity AS capacity, c.enrolled AS enrolled FROM Course c")
    Page<CourseResponseDto> findAllCoursesByPage(Pageable pageable);
}

