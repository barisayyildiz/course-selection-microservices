package com.courseselection.courseservice.service;

import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaCourseListener {
    private static final String topic = "course-operation";

    @Autowired
    private CourseService courseService;

    @KafkaListener(id = "courseServiceCourseConsumer", topics = topic, groupId = "course-service", autoStartup = "true")
    public void listen(GenericData.Record record) {
        GenericData.Record kafkaCourse = (GenericData.Record) record.get("course");
        switch (record.get("operation").toString()) {
            case "CREATE":
                courseService.addCourse(kafkaCourse);
                break;
            case "UPDATE":
                courseService.updateCourse(kafkaCourse);
                break;
            case "DELETE":
                courseService.deleteCourse(kafkaCourse);
                break;
        }
    }
}
