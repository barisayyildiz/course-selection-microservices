package com.courseselection.professorservice.utility;

import com.courseselection.professorservice.dtos.UserProfileResponseDTO;
import com.courseselection.professorservice.model.Professor;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtility {
    public UserProfileResponseDTO hideUserDetails(Professor professor) {
        return UserProfileResponseDTO
                .builder()
                .email(professor.getEmail())
                .name(professor.getName())
                .build();
    }
}
