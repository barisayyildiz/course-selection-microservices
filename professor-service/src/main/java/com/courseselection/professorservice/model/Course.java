package com.courseselection.professorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "courses",
        uniqueConstraints = {
                @UniqueConstraint(name="uc_courseid", columnNames = {"id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="code")
    private String code;

    @Column(name="capacity")
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name="professorid", referencedColumnName = "id")
    private Professor professor;
}
