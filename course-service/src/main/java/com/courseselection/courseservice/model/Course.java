package com.courseselection.courseservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "courses"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @Column
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="code")
    private String code;

    @ManyToOne
    @JoinColumn(name="professorid", referencedColumnName = "id")
    private Professor professor;

    @Column(name="capacity")
    private Integer capacity;

    @Column(name="enrolled")
    private Integer enrolled;

}
