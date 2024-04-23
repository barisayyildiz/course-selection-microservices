package org.example.kafka.producer;

import org.apache.kafka.clients.producer.*;
import java.util.Properties;

public class KafkaProducerExample {
    public static void produce() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:19092"); // Change this to your Kafka broker address
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer(props);
        String topicName = "chat_room";

        try {
            for(int i=0; i<10; i++) {
                String message = "message #" + i;
                producer.send(new ProducerRecord<>(topicName, message), (recordMetadata, e) -> {
                    if(e == null) {
                        System.out.println(
                                "Received new metadata. \n" +
                                "Topic:" + recordMetadata.topic() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp()
                        );
                    } else {
                        System.err.println("Error while producing" + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
