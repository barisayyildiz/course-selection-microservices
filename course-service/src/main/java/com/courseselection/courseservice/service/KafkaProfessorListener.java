package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Professor;
import com.courseselection.kafkatypes.ProfessorEvent;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaProfessorListener {
    private static final String topic = "professor-operation";

    @Autowired
    private ProfessorService professorService;

    @KafkaListener(id = "courseServiceConsumer", topics = topic, groupId = "course-service", autoStartup = "true")
    public void listen(GenericData.Record record) {
        GenericData.Record kafkaProfessor = (GenericData.Record) record.get("professor");
        switch (record.get("operation").toString()) {
            case "CREATE":
                professorService.addProfessor(kafkaProfessor);
                break;
            case "UPDATE":
                professorService.updateProfessor(kafkaProfessor);
                break;
        }
    }

}
