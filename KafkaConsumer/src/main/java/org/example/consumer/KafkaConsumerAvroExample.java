package org.example.consumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerAvroExample {
    public static void consume() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put("schema.registry.url", "http://localhost:18081");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

//        KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<String, GenericRecord>(props);
//        String topic = "chat_room";
//        consumer.subscribe(List.of(topic));

        for(int i=0; i<3; i++) {
            new Thread(() -> {
                KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<String, GenericRecord>(props);
                String topic = "chat_room";
                consumer.subscribe(List.of(topic));

                try {
                    while(true) {
                        ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(100));
                        for(ConsumerRecord record: records) {
                            System.out.println(
                                    "Consumer thread number: " + Thread.currentThread().getName() +
                                    " received message: " + record.value() +
                                    " from partition: " + record.partition() +
                                    " with offset: " + record.offset() +
                                    " ==============================");
                        }
                    }
                } finally {
                    consumer.close();
                }
            }).run();
        }

//        try {
//            while (true) {
//                ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(100));
//                for (ConsumerRecord<String, GenericRecord> record : records) {
//                    System.out.println(
//                            "Consumer number: " + consumerNumber +
//                                    "Received message: " + record.value() +
//                                    " from partition: " + record.partition() +
//                                    " with offset: " + record.offset() +
//                                    " ==============================");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            consumer.close();
//        }

    }
}
