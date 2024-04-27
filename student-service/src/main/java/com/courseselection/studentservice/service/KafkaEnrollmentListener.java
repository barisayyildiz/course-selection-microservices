package com.courseselection.studentservice.service;

import com.courseselection.kafkatypes.EnrollmentDropResponse;
import com.courseselection.studentservice.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEnrollmentListener {
    private static final String topic = Constants.ENROLLMENT_RESPONSE;

    @Autowired
    private StudentService studentService;

    @KafkaListener(id = "studentServiceEnrollmentConsumer", topics = topic, groupId = "student-service", autoStartup = "true")
    public void listen(EnrollmentDropResponse enrollmentDropResponse) {
        studentService.processEnrollmentResponse(enrollmentDropResponse);
    }
}
