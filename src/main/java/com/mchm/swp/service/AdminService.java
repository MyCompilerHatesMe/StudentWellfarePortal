package com.mchm.swp.service;

import com.mchm.swp.model.dto.response.EnrollmentConnectionResponse;
import com.mchm.swp.model.dto.response.WardConnectionResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.utils.DtoMapper;
import com.mchm.swp.utils.ProfileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final FacultySubjectEnrollmentRepo enrollmentRepo;
    private final ProfileUtils utils;
    private final DtoMapper mapper;
    private final ParentProfileRepo parentRepo;

    @Transactional
    public EnrollmentConnectionResponse enroll(String studentUsername, String facultyUsername, String subject) {

        if (enrollmentRepo.existsByFaculty_AuthUser_UsernameAndStudent_AuthUser_UsernameAndSubject(
                facultyUsername, studentUsername, subject
        )) throw new IllegalStateException("Student-Faculty-Subject Enrollment already exists");

        StudentProfile studentProfile = utils.getStudentProfile(studentUsername);
        FacultyProfile facultyProfile = utils.getVerifiedFacultyProfile(facultyUsername);

        FacultySubjectEnrollment toBeSaved = FacultySubjectEnrollment.builder()
                .faculty(facultyProfile)
                .student(studentProfile)
                .subject(subject).build();

        FacultySubjectEnrollment saved = enrollmentRepo.save(toBeSaved);

        return mapper.toResponse(saved);
    }

    @Transactional
    public WardConnectionResponse connect(String wardUsername, String parentUsername) {
        ParentProfile parent = utils.getParentProfile(parentUsername);
        StudentProfile student = utils.getStudentProfile(wardUsername);

        if (parent.getChildren().contains(student))
            throw new IllegalStateException("Parent-Ward Connection already exists");
        parent.getChildren().add(student);
        ParentProfile saved = parentRepo.save(parent);
        return new WardConnectionResponse(student.getName(), saved.getName());
    }
}
