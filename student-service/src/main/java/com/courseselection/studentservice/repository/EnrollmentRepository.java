package com.courseselection.studentservice.repository;

import com.courseselection.studentservice.model.Enrollment;
import com.courseselection.studentservice.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
}
