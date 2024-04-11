package com.courseselection.professorservice.service;

import com.courseselection.kafkatypes.CourseOperation;
import com.courseselection.professorservice.dtos.CourseCreationDto;
import com.courseselection.professorservice.dtos.CourseRequestDto;
import com.courseselection.professorservice.dtos.CourseUpdateDto;
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
    private ProfessorRepository professorRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

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
                        .capacity(course.getCapacity())
                        .build()
                )
                .toList();
    }

    public CourseCreationDto createCourse(CourseCreationDto courseCreationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Professor currentProfessor = (Professor) authentication.getPrincipal();

        com.courseselection.kafkatypes.Course kafkaCourse = new com.courseselection.kafkatypes.Course();
        kafkaCourse.setProfessorId(currentProfessor.getId());
        kafkaCourse.setName(courseCreationDto.getName());
        kafkaCourse.setCapacity(courseCreationDto.getCapacity());
        kafkaCourse.setEnrolled(0);
        kafkaCourse.setCode(courseCreationDto.getCode());

        CourseOperation courseOperation = new CourseOperation();
        courseOperation.setCourse(kafkaCourse);
        courseOperation.setOperation("CREATE");

        kafkaProducer.sendMessage(courseOperation);

        Course course = Course
                .builder()
                .name(courseCreationDto.getName())
                .code(courseCreationDto.getCode())
                .professor(currentProfessor)
                .build();
        courseRepository.save(course);

        return courseCreationDto;
    }

    public CourseRequestDto updateCourse(Integer id, CourseUpdateDto courseUpdateDtos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Professor currentProfessor = (Professor) authentication.getPrincipal();
        CourseRequestDto courseRequestDto = null;
        Course savedCourse = null;

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            if(Objects.nonNull(courseUpdateDtos.getName())) {
                course.setName(courseUpdateDtos.getName().get());
            }
            if(Objects.nonNull(courseUpdateDtos.getCode())) {
                course.setCode(courseUpdateDtos.getCode().get());
            }
            if(Objects.nonNull(courseUpdateDtos.getCapacity())) {
                course.setCapacity(courseUpdateDtos.getCapacity().get());
            }
            savedCourse = courseRepository.save(course);
            courseRequestDto = CourseRequestDto
                    .builder()
                    .id(savedCourse.getId())
                    .name(savedCourse.getName())
                    .code(savedCourse.getCode())
                    .capacity(savedCourse.getCapacity())
                    .build();
        }

        if(Objects.nonNull(savedCourse)) {
            com.courseselection.kafkatypes.Course kafkaCourse = new com.courseselection.kafkatypes.Course();
            kafkaCourse.setProfessorId(currentProfessor.getId());
            kafkaCourse.setName(savedCourse.getName());
            kafkaCourse.setCapacity(savedCourse.getCapacity());
            kafkaCourse.setCode(savedCourse.getCode());

            CourseOperation courseOperation = new CourseOperation();
            courseOperation.setCourse(kafkaCourse);
            courseOperation.setOperation("UPDATE");

            kafkaProducer.sendMessage(courseOperation);
        }

        return courseRequestDto;
    }

    public boolean deleteCourse(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Professor currentProfessor = (Professor) authentication.getPrincipal();

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if(optionalCourse.isPresent()) {
            CourseOperation courseOperation = new CourseOperation();
            courseOperation.setOperation("DELETE");

            kafkaProducer.sendMessage(courseOperation);

            courseRepository.deleteById(optionalCourse.get().getId());
            return true;
        }
        return false;
    }

}
