package com.courseselection.courseservice.service;

import com.courseselection.kafkatypes.EnrollmentDropRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaEnrollmentListener {
    private static final Logger logger = Logger.getLogger(KafkaEnrollmentListener.class.getName());
    private static final String topic = "enrollment-request";
    @Autowired
    private CourseService courseService;

    @KafkaListener(id = "courseServiceEnrollmentConsumer", topics = topic, groupId = "course-service", autoStartup = "true")
    public void listen(EnrollmentDropRequest enrollmentDropRequest) {
        logger.info("EnrollmentDropRequest message consumed from the topic " + topic);
        courseService.processEnrollmentRequest(enrollmentDropRequest);
    }
}
