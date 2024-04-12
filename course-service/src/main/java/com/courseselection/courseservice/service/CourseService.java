package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Course;
import com.courseselection.courseservice.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> searchCoursesByName(String query) {
        return courseRepository.searchByName(query);
    }
}
