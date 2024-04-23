package com.courseselection.studentservice.service;

import com.courseselection.kafkatypes.EnrollmentDropRequest;
import com.courseselection.studentservice.dtos.StudentResponseDto;
import com.courseselection.studentservice.dtos.UpdateStudentDto;
import com.courseselection.studentservice.model.Enrollment;
import com.courseselection.studentservice.model.EnrollmentId;
import com.courseselection.studentservice.model.Student;
import com.courseselection.studentservice.repository.EnrollmentRepository;
import com.courseselection.studentservice.repository.StudentRepository;
import com.courseselection.studentservice.utility.Constants;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Student student = (Student) authentication.getPrincipal();

        Optional<Enrollment> durableEnrollment = enrollmentRepository.findById(
                EnrollmentId.builder()
                        .studentId(student.getId())
                        .courseId(courseId)
                        .build()
        );

        if(durableEnrollment.isPresent()) {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Student student = (Student) authentication.getPrincipal();

        Optional<Enrollment> durableEnrollment = enrollmentRepository.findById(
                EnrollmentId.builder()
                        .studentId(student.getId())
                        .courseId(courseId)
                        .build()
        );

        if(durableEnrollment.isEmpty()) {
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

    public void processEnrollmentResponse(GenericData.Record record) {
        Optional<Student> student = studentRepository.findById((Integer) record.get("student_id"));

        String type = record.get("type").toString();
        String status = record.get("status").toString();
        Integer courseId = (Integer) record.get("course_id");
        Integer studentId = (Integer) record.get("student_id");

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
                                            .studentId((Integer) record.get("student_id"))
                                            .courseId((Integer) record.get("course_id"))
                                            .build()
                            )
                            .student(durableStudent)
                            .build();
                    enrollmentRepository.save(enrollment);
                    System.out.println("Student enrolled to the course");
                } else {
                    System.out.println("Student enrollment failed: " + record.get("message"));
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
