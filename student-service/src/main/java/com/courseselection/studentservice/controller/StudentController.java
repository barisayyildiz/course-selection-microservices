package com.courseselection.studentservice.controller;

import com.courseselection.studentservice.dtos.EnrollmentResponseDto;
import com.courseselection.studentservice.dtos.StudentResponseDto;
import com.courseselection.studentservice.dtos.UpdateStudentDto;
import com.courseselection.studentservice.repository.StudentRepository;
import com.courseselection.studentservice.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @GetMapping("/student")
    public ResponseEntity<StudentResponseDto> getCurrentStudent() {
        return new ResponseEntity<StudentResponseDto>(studentService.getCurrentStudent(), HttpStatus.OK);
    }

    @PutMapping("/student")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @Valid @RequestBody UpdateStudentDto updateStudentDto
    ) {
        StudentResponseDto savedStudent = studentService.updateStudent(updateStudentDto);
        return new ResponseEntity<StudentResponseDto>(savedStudent, HttpStatus.OK);
    }

    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentResponseDto> enrollCourse(@RequestParam Integer course_id) {
        if(studentService.enrollCourse(course_id)) {
            return new ResponseEntity<>(
                    EnrollmentResponseDto.builder().message("Enrollment request send").build(),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    EnrollmentResponseDto.builder().message("Student already enrolled to the course").build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/drop")
    public ResponseEntity<EnrollmentResponseDto> dropCourse(@RequestParam Integer course_id) {
        if(studentService.dropCourse(course_id)) {
            return new ResponseEntity<>(
                    EnrollmentResponseDto.builder().message("Drop request send").build(),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    EnrollmentResponseDto.builder().message("Student not enrolled to the course").build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
