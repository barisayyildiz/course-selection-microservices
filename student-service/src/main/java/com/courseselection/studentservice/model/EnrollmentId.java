package com.courseselection.studentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EnrollmentId implements Serializable {
    @Column(name = "courseId")
    private Integer courseId;

    @Column(name = "studentId")
    private Integer studentId;

    @Override
    public int hashCode() {
        return Objects.hash(courseId, studentId);
    }
}
