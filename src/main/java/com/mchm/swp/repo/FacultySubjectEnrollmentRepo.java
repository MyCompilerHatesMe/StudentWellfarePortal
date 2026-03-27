package com.mchm.swp.repo;

import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FacultySubjectEnrollmentRepo extends JpaRepository<FacultySubjectEnrollment, Long> {
    Collection<FacultySubjectEnrollment> findByStudent_AuthUser_Username(String authUsername);

    Collection<FacultySubjectEnrollment> findByFaculty_AuthUser_Username(String authUsername);
}
