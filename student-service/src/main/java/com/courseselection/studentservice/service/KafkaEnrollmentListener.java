package com.courseselection.studentservice.service;

import com.courseselection.kafkatypes.EnrollmentDropResponse;
import com.courseselection.studentservice.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaEnrollmentListener {
    private static final Logger logger = Logger.getLogger(KafkaEnrollmentListener.class.getName());
    private static final String topic = Constants.ENROLLMENT_RESPONSE;

    @Autowired
    private StudentService studentService;

    @KafkaListener(id = "studentServiceEnrollmentConsumer", topics = topic, groupId = "student-service", autoStartup = "true")
    public void listen(EnrollmentDropResponse enrollmentDropResponse) {
        logger.info("EnrollmentDropResponse message consumed from the topic " + topic + ", " + enrollmentDropResponse);
        studentService.processEnrollmentResponse(enrollmentDropResponse);
    }
}
