package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Course;
import com.courseselection.courseservice.model.Professor;
import com.courseselection.courseservice.repository.CourseRepository;
import com.courseselection.courseservice.repository.ProfessorRepository;
import com.courseselection.courseservice.utility.Constants;
import com.courseselection.kafkatypes.EnrollmentDropResponse;
import com.courseselection.kafkatypes.EnrollmentResponse;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

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

    @Transactional
    public void processEnrollmentRequest(GenericData.Record record) {
        System.out.println("inside processEnrollmentRequest");

        Integer courseId = (Integer) record.get("course_id");
        Integer studentId = (Integer) record.get("student_id");
        String type = record.get("type").toString();

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
