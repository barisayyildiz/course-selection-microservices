package com.courseselection.studentservice.service;

import com.courseselection.kafkatypes.EnrollmentDropRequest;
import com.courseselection.kafkatypes.EnrollmentDropResponse;
import com.courseselection.studentservice.dtos.StudentResponseDto;
import com.courseselection.studentservice.dtos.UpdateStudentDto;
import com.courseselection.studentservice.model.Enrollment;
import com.courseselection.studentservice.model.EnrollmentId;
import com.courseselection.studentservice.model.Student;
import com.courseselection.studentservice.repository.EnrollmentRepository;
import com.courseselection.studentservice.repository.StudentRepository;
import com.courseselection.studentservice.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class StudentService {
    private static final Logger logger = Logger.getLogger(StudentService.class.getName());
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    KafkaProducer kafkaProducer;

    public StudentResponseDto getCurrentStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Student authStudent = (Student) authentication.getPrincipal();
        return StudentResponseDto
                .builder()
                .id(authStudent.getId())
                .name(authStudent.getName())
                .email(authStudent.getEmail())
                .build();
    }

    public StudentResponseDto updateStudent(UpdateStudentDto updateStudentDto) {
        logger.info("Updating the student: " + updateStudentDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Student student = (Student) authentication.getPrincipal();

        student.setName(updateStudentDto.getName());

        Student savedStudent = studentRepository.save(student);
        return StudentResponseDto
                .builder()
                .id(savedStudent.getId())
                .name(savedStudent.getName())
                .email(savedStudent.getEmail())
                .build();
    }

    public Boolean enrollCourse(Integer courseId) {
        logger.info("Enrolling to course: " + courseId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Student student = (Student) authentication.getPrincipal();

        Optional<Enrollment> durableEnrollment = enrollmentRepository.findById(
                EnrollmentId.builder()
                        .studentId(student.getId())
                        .courseId(courseId)
                        .build()
        );

        if(durableEnrollment.isPresent()) {
            logger.info("Course is not found : " + courseId);
            return false;
        }

        EnrollmentDropRequest enrollmentRequest = EnrollmentDropRequest
                .newBuilder()
                .setType("ENROLLMENT")
                .setCourseId(courseId)
                .setStudentId(student.getId())
                .build();
        kafkaProducer.sendMessage(Constants.ENROLLMENT_REQUEST, enrollmentRequest);
        return true;
    }

    public Boolean dropCourse(Integer courseId) {
        logger.info("Dropping the course: " + courseId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Student student = (Student) authentication.getPrincipal();

        Optional<Enrollment> durableEnrollment = enrollmentRepository.findById(
                EnrollmentId.builder()
                        .studentId(student.getId())
                        .courseId(courseId)
                        .build()
        );

        if(durableEnrollment.isEmpty()) {
            logger.info("Course is not found : " + courseId);
            return false;
        }

        EnrollmentDropRequest enrollmentRequest = EnrollmentDropRequest
                .newBuilder()
                .setType("DROP")
                .setCourseId(courseId)
                .setStudentId(student.getId())
                .build();
        kafkaProducer.sendMessage(Constants.ENROLLMENT_REQUEST, enrollmentRequest);
        return true;
    }

    public void processEnrollmentResponse(EnrollmentDropResponse enrollmentDropResponse) {
        logger.info("Processing enrollment response: " + enrollmentDropResponse);
        Optional<Student> student = studentRepository.findById(enrollmentDropResponse.getStudentId());

        String type = enrollmentDropResponse.getType().toString();
        String status = enrollmentDropResponse.getStatus().toString();
        Integer courseId = enrollmentDropResponse.getCourseId();
        Integer studentId = enrollmentDropResponse.getStudentId();

        if(student.isEmpty()) {
            System.out.println("Student not found");
        } else {
            Student durableStudent = student.get();
            if(type.equals("ENROLLMENT")) {
                if(status.equals("ACCEPTED")) {
                    Enrollment enrollment = Enrollment
                            .builder()
                            .id(
                                    EnrollmentId
                                            .builder()
                                            .studentId(studentId)
                                            .courseId(courseId)
                                            .build()
                            )
                            .student(durableStudent)
                            .build();
                    enrollmentRepository.save(enrollment);
                    System.out.println("Student enrolled to the course");
                } else {
                    System.out.println("Student enrollment failed: " + enrollmentDropResponse.getMessage());
                }
            } else {
                if(status.equals("ACCEPTED")) {
                    Optional<Enrollment> enrollment = enrollmentRepository.findById(
                            EnrollmentId.builder()
                                    .studentId(studentId)
                                    .courseId(courseId)
                                    .build()
                    );
                    enrollment.ifPresent(value -> enrollmentRepository.delete(value));
                }
            }
        }
    }
}
