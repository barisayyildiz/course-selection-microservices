package com.courseselection.courseservice.service;

import com.courseselection.courseservice.model.Professor;
import com.courseselection.kafkatypes.ProfessorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaProfessorListener {
    private static final Logger logger = Logger.getLogger(KafkaProfessorListener.class.getName());
    private static final String topic = "professor-operation";

    @Autowired
    private ProfessorService professorService;

    @KafkaListener(id = "courseServiceProfessorConsumer", topics = topic, groupId = "course-service", autoStartup = "true")
    public void listen(ProfessorEvent professorEvent) {
        logger.info("ProfessorEvent message consumed from the topic " + topic);
        switch (professorEvent.getOperation().toString()) {
            case "CREATE":
                professorService.addProfessor(professorEvent.getProfessor());
                break;
            case "UPDATE":
                professorService.updateProfessor(professorEvent.getProfessor());
                break;
        }
    }

}
