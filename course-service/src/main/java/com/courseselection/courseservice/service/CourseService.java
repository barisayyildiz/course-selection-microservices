package com.courseselection.courseservice.service;

import com.courseselection.courseservice.dto.CourseResponseDto;
import com.courseselection.courseservice.model.Course;
import com.courseselection.courseservice.model.Professor;
import com.courseselection.courseservice.repository.CourseRepository;
import com.courseselection.courseservice.repository.ProfessorRepository;
import com.courseselection.courseservice.utility.Constants;
import com.courseselection.kafkatypes.EnrollmentDropRequest;
import com.courseselection.kafkatypes.EnrollmentDropResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CourseService {
    private static final Logger logger = Logger.getLogger(CourseService.class.getName());
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    public Page<CourseResponseDto> findAllCourses(Integer pageNumber, Integer pageSize) {
        logger.info("Find courses on page: " + (pageNumber + 1) + ", with size: " + pageSize);
        return courseRepository.findAllCoursesByPage(PageRequest.of(pageNumber, pageSize));
    }

    public List<Course> searchCoursesByName(String query) {
        return courseRepository.searchByName(query);
    }

    public void addCourse(com.courseselection.kafkatypes.Course kafkaCourse) {
        logger.info("Adding the course: " + kafkaCourse.toString());
        Integer professorId = kafkaCourse.getProfessorId();
        Optional<Professor> durableProfessor = professorRepository.findById(professorId);
        if(durableProfessor.isPresent()) {
            Course course = Course
                    .builder()
                    .id(kafkaCourse.getId())
                    .name(kafkaCourse.getName().toString())
                    .code(kafkaCourse.getCode().toString())
                    .capacity(kafkaCourse.getCapacity())
                    .enrolled(0)
                    .professor(durableProfessor.get())
                    .build();
            courseRepository.save(course);
        }
    }

    public void updateCourse(com.courseselection.kafkatypes.Course kafkaCourse) {
        logger.info("Updating the course: " + kafkaCourse.toString());
        Integer professorId = kafkaCourse.getProfessorId();
        Optional<Professor> durableProfessor = professorRepository.findById(professorId);
        Optional<Course> optionalCourse = courseRepository.findById(
                kafkaCourse.getId()
        );

        if(durableProfessor.isPresent() && optionalCourse.isPresent()) {
            Course course = Course
                    .builder()
                    .id(optionalCourse.get().getId())
                    .name(kafkaCourse.getName().toString())
                    .code(kafkaCourse.getCode().toString())
                    .capacity(kafkaCourse.getCapacity())
                    .enrolled(optionalCourse.get().getEnrolled())
                    .professor(durableProfessor.get())
                    .build();
            courseRepository.save(course);
        }
    }

    public void deleteCourse(com.courseselection.kafkatypes.Course kafkaCourse) {
        logger.info("Deleting the course: " + kafkaCourse.toString());
        Optional<Course> optionalCourse = courseRepository.findById(
                kafkaCourse.getId()
        );
        optionalCourse.ifPresent(course -> courseRepository.delete(course));
    }

    @Transactional
    public void processEnrollmentRequest(EnrollmentDropRequest enrollmentDropRequest) {
        logger.info("Processing enrollment request: " + enrollmentDropRequest.toString());
        System.out.println("inside processEnrollmentRequest");

        int courseId = enrollmentDropRequest.getCourseId();
        int studentId = enrollmentDropRequest.getStudentId();
        String type = enrollmentDropRequest.getType().toString();

        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        EnrollmentDropResponse enrollmentResponse = new EnrollmentDropResponse();
        enrollmentResponse.setType(type);
        enrollmentResponse.setCourseId(courseId);
        enrollmentResponse.setStudentId(studentId);

        if(optionalCourse.isPresent()) {
            Course durableCourse = optionalCourse.get();
            if(type.equals("ENROLLMENT")) {
                if(durableCourse.getEnrolled() < durableCourse.getCapacity()) {
                    durableCourse.setEnrolled(durableCourse.getEnrolled() + 1);
                    enrollmentResponse.setStatus("ACCEPTED");
                    enrollmentResponse.setMessage("Student enrolled to the course " + durableCourse.getName());
                } else {
                    enrollmentResponse.setStatus("REJECTED");
                    enrollmentResponse.setMessage("Course capacity is full");
                }
            } else {
                durableCourse.setEnrolled(durableCourse.getEnrolled() - 1);
                enrollmentResponse.setStatus("ACCEPTED");
                enrollmentResponse.setMessage("Student dropped the course " + durableCourse.getName());
            }
        } else {
            enrollmentResponse.setStatus("REJECTED");
            enrollmentResponse.setMessage("Course not found");
        }
        kafkaProducer.sendMessage(Constants.ENROLLMENT_RESPONSE, enrollmentResponse);
    }
}
