package com.mchm.swp.repo;

import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultySubjectEnrollmentRepo extends JpaRepository<FacultySubjectEnrollment, Long> {
}
