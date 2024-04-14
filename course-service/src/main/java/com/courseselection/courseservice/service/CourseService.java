package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Course;
import com.courseselection.courseservice.model.Professor;
import com.courseselection.courseservice.repository.CourseRepository;
import com.courseselection.courseservice.repository.ProfessorRepository;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> searchCoursesByName(String query) {
        return courseRepository.searchByName(query);
    }

    public void addCourse(GenericData.Record record) {
        Integer professorId = Integer.parseInt(record.get("professorId").toString());
        Optional<Professor> durableProfessor = professorRepository.findById(professorId);
        if(durableProfessor.isPresent()) {
            Course course = Course
                    .builder()
                    .id(Integer.parseInt(record.get("id").toString()))
                    .name(record.get("name").toString())
                    .code(record.get("code").toString())
                    .capacity(Integer.parseInt(record.get("capacity").toString()))
                    .enrolled(0)
                    .professor(durableProfessor.get())
                    .build();
            courseRepository.save(course);
        }
    }

    public void updateCourse(GenericData.Record record) {
        Integer professorId = Integer.parseInt(record.get("professorId").toString());
        Optional<Professor> durableProfessor = professorRepository.findById(professorId);
        Optional<Course> optionalCourse = courseRepository.findById(
                Integer.parseInt(record.get("id").toString())
        );

        if(durableProfessor.isPresent() && optionalCourse.isPresent()) {
            Course course = Course
                    .builder()
                    .id(optionalCourse.get().getId())
                    .name(record.get("name").toString())
                    .code(record.get("code").toString())
                    .capacity(Integer.parseInt(record.get("capacity").toString()))
                    .enrolled(optionalCourse.get().getEnrolled())
                    .professor(durableProfessor.get())
                    .build();
            courseRepository.save(course);
        }
    }

    public void deleteCourse(GenericData.Record record) {
        Optional<Course> optionalCourse = courseRepository.findById(
                Integer.parseInt(record.get("id").toString())
        );
        optionalCourse.ifPresent(course -> courseRepository.delete(course));
    }
}
