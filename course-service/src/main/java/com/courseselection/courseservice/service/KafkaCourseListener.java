package com.courseselection.courseservice.service;

import com.courseselection.courseservice.controller.CourseController;
import com.courseselection.kafkatypes.CourseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaCourseListener {
    private static final Logger logger = Logger.getLogger(KafkaCourseListener.class.getName());
    private static final String topic = "course-operation";

    @Autowired
    private CourseService courseService;

    @KafkaListener(id = "courseServiceCourseConsumer", topics = topic, groupId = "course-service", autoStartup = "true")
    public void listen(CourseEvent courseEvent) {
        logger.info("CourseEvent message consumed from the topic " + topic);
        switch (courseEvent.getOperation().toString()) {
            case "CREATE":
                courseService.addCourse(courseEvent.getCourse());
                break;
            case "UPDATE":
                courseService.updateCourse(courseEvent.getCourse());
                break;
            case "DELETE":
                courseService.deleteCourse(courseEvent.getCourse());
                break;
        }
    }
}
