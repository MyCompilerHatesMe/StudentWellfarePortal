package com.mchm.swp.service;

import com.mchm.swp.model.dto.response.ConnectionResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.utils.DtoMapper;
import com.mchm.swp.utils.ProfileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final FacultySubjectEnrollmentRepo enrollmentRepo;
    private final ProfileUtils utils;
    private final DtoMapper mapper;

    public ConnectionResponse connect(String studentUsername, String facultyUsername, String subject) {

        if (enrollmentRepo.existsByFaculty_AuthUser_UsernameAndStudent_AuthUser_UsernameAndSubject(
                facultyUsername, studentUsername, subject
        )) throw new IllegalStateException("Student-Faculty-Subject Enrollment already exists");

        StudentProfile studentProfile = utils.getProfile(studentUsername);
        FacultyProfile facultyProfile = utils.getVerifiedFacultyProfile(facultyUsername);

        FacultySubjectEnrollment toBeSaved = FacultySubjectEnrollment.builder()
                .faculty(facultyProfile)
                .student(studentProfile)
                .subject(subject).build();

        FacultySubjectEnrollment saved = enrollmentRepo.save(toBeSaved);

        return mapper.toResponse(saved);

    }
}
