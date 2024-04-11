package com.courseselection.professorservice.service;

import com.courseselection.professorservice.dtos.CourseRequestDto;
import com.courseselection.professorservice.dtos.UpdateProfessorRequestDto;
import com.courseselection.professorservice.model.Course;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.repository.CourseRepository;
import com.courseselection.professorservice.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfessorService {
    @Autowired
    ProfessorRepository professorRepository;
    @Autowired
    CourseRepository courseRepository;

    public Optional<Professor> getProfessorById(Integer id) {
        return professorRepository.findById(id);
    }

    public Professor updateProfessor(UpdateProfessorRequestDto updateProfessorRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Professor currentProfessor = (Professor) authentication.getPrincipal();

        if(Objects.nonNull(updateProfessorRequestDto.getName())) {
            currentProfessor.setName(updateProfessorRequestDto.getName().orElse(""));
        }

        return professorRepository.save(currentProfessor);
    }

    public List<CourseRequestDto> getCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Professor currentProfessor = (Professor) authentication.getPrincipal();

        List<Course> courseList = courseRepository.findByProfessorId(currentProfessor.getId());

        return courseList.stream()
                .map(course -> CourseRequestDto
                        .builder()
                        .id(course.getId())
                        .name(course.getName())
                        .code(course.getCode())
                        .build()
                )
                .toList();
    }

}
