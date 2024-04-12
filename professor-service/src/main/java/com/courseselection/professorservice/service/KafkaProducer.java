package com.courseselection.professorservice.service;

import com.courseselection.kafkatypes.CourseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {
    private static final String TOPIC = "course-operation";
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, Object courseOperation) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, courseOperation);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("produced event to topic " + TOPIC);
            } else {
                ex.printStackTrace(System.out);
            }
        });


    }
}
