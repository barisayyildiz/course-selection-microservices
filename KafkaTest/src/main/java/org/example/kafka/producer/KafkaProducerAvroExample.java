package org.example.kafka.producer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

public class KafkaProducerAvroExample {
    public static void produce() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:19092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", "http://localhost:18081");

        Producer producer = new KafkaProducer(props);
        String topicName = "chat_room";

        // Define Avro schema
        String avroSchemaString = "{\"type\":\"record\",\"name\":\"message\",\"fields\":[{\"name\":\"sender\",\"type\":\"string\"},{\"name\":\"receiver\",\"type\":\"string\"},{\"name\":\"read\",\"type\":\"boolean\"}]}";
        Schema.Parser parser = new Schema.Parser();
        Schema avroSchema = parser.parse(avroSchemaString);

        GenericRecord avroRecord = new GenericData.Record(avroSchema);
        avroRecord.put("sender", "Bob");
        avroRecord.put("receiver", "Alice");
        avroRecord.put("read", false);

        try {
            String key = UUID.randomUUID().toString();
            ProducerRecord<Object, Object> record = new ProducerRecord<>(topicName, key, avroRecord);
            producer.send(record);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
