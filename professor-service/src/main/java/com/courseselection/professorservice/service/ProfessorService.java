package com.courseselection.professorservice.service;

import com.courseselection.kafkatypes.CourseEvent;
import com.courseselection.kafkatypes.ProfessorEvent;
import com.courseselection.professorservice.dtos.CourseCreationDto;
import com.courseselection.professorservice.dtos.CourseRequestDto;
import com.courseselection.professorservice.dtos.CourseUpdateDto;
import com.courseselection.professorservice.dtos.UpdateProfessorRequestDto;
import com.courseselection.professorservice.model.Course;
import com.courseselection.professorservice.model.Professor;
import com.courseselection.professorservice.repository.CourseRepository;
import com.courseselection.professorservice.repository.ProfessorRepository;
import com.courseselection.professorservice.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ProfessorService {
    private static final Logger logger = Logger.getLogger(ProfessorService.class.getName());
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
        logger.info("Updating the professor " + updateProfessorRequestDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Professor currentProfessor = (Professor) authentication.getPrincipal();

        currentProfessor.setName(updateProfessorRequestDto.getName());
        com.courseselection.kafkatypes.Professor professor = new com.courseselection.kafkatypes.Professor();
        professor.setId(currentProfessor.getId());
        professor.setName(currentProfessor.getName());
        ProfessorEvent professorEvent = new ProfessorEvent("UPDATE", professor);
        kafkaProducer.sendMessage(Constants.PROFESSOR_OPERATION, professorEvent);

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

        Course course = Course
                .builder()
                .name(courseCreationDto.getName())
                .code(courseCreationDto.getCode())
                .capacity(courseCreationDto.getCapacity())
                .professor(currentProfessor)
                .build();
        Course savedCourse = courseRepository.save(course);

        com.courseselection.kafkatypes.Course kafkaCourse = new com.courseselection.kafkatypes.Course();
        kafkaCourse.setId(savedCourse.getId());
        kafkaCourse.setProfessorId(savedCourse.getProfessor().getId());
        kafkaCourse.setName(courseCreationDto.getName());
        kafkaCourse.setCapacity(courseCreationDto.getCapacity());
        kafkaCourse.setCode(courseCreationDto.getCode());

        CourseEvent courseEvent = new CourseEvent("CREATE", kafkaCourse);
        kafkaProducer.sendMessage(Constants.COURSE_OPERATION, courseEvent);

        return courseCreationDto;
    }

    public CourseRequestDto updateCourse(Integer id, CourseUpdateDto courseUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Professor currentProfessor = (Professor) authentication.getPrincipal();
        CourseRequestDto courseRequestDto = null;
        Course savedCourse = null;

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setName(courseUpdateDto.getName());
            course.setCode(courseUpdateDto.getCode());
            course.setCapacity(courseUpdateDto.getCapacity());
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
            kafkaCourse.setId(savedCourse.getId());
            kafkaCourse.setName(savedCourse.getName());
            kafkaCourse.setCode(savedCourse.getCode());
            kafkaCourse.setCapacity(savedCourse.getCapacity());
            kafkaCourse.setProfessorId(currentProfessor.getId());

            CourseEvent courseEvent = new CourseEvent("UPDATE", kafkaCourse);
            kafkaProducer.sendMessage(Constants.COURSE_OPERATION, courseEvent);
        }

        return courseRequestDto;
    }

    public boolean deleteCourse(Integer id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if(optionalCourse.isPresent()) {
            com.courseselection.kafkatypes.Course kafkaCourse = new com.courseselection.kafkatypes.Course();
            kafkaCourse.setId(optionalCourse.get().getId());
            CourseEvent courseEvent = new CourseEvent("DELETE", kafkaCourse);

            kafkaProducer.sendMessage(Constants.COURSE_OPERATION, courseEvent);
            courseRepository.deleteById(optionalCourse.get().getId());
            return true;
        }
        return false;
    }
}
