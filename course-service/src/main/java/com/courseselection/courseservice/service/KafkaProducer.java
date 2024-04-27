package com.courseselection.courseservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class KafkaProducer {
    private static final Logger logger = Logger.getLogger(KafkaProducer.class.getName());
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, Object courseOperation) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, courseOperation);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Produced event to topic " + topic);
            } else {
                logger.severe("Error during producing event to topic " + topic + ", " + ex.getMessage());
                ex.printStackTrace(System.out);
            }
        });


    }
}
