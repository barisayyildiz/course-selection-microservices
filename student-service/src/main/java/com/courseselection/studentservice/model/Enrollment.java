package com.courseselection.studentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name="enrolled_courses"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne
    @JoinColumn(name = "studentId", referencedColumnName = "id", insertable = false, updatable = false)
    private Student student;
}
