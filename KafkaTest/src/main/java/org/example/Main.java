package org.example;

import org.example.kafka.consumer.KafkaConsumerAvroExample;
import org.example.kafka.consumer.KafkaConsumerExample;
import org.example.kafka.producer.KafkaProducerAvroExample;
import org.example.kafka.producer.KafkaProducerExample;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        KafkaProducerExample.produce();
//        KafkaConsumerExample.consume();
//        KafkaProducerAvroExample.produce();
//        KafkaConsumerAvroExample.consume(1);
        Thread tKafka = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        KafkaProducerAvroExample.produce();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        tKafka.run();
        tKafka.join();
    }
}