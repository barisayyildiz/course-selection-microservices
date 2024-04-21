package com.courseselection.courseservice.service;

import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEnrollmentListener {
    private static final String topic = "enrollment-request";

    @Autowired
    private CourseService courseService;

    @KafkaListener(id = "courseServiceEnrollmentConsumer", topics = topic, groupId = "course-service", autoStartup = "true")
    public void listen(GenericData.Record record) {
        System.out.println("Listening enrollment topic");
        courseService.processEnrollmentRequest(record);
    }
}
