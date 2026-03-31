package com.mchm.swp.service;

import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import com.mchm.swp.utils.DtoMapper;
import com.mchm.swp.utils.ProfileUtils;
import com.mchm.swp.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultySubjectEnrollmentRepo enrollmentRepo;
    private final StudentProfileRepo studentRepo;
    private final ProfileUtils utils;
    private final DtoMapper mapper;

    public FacultyProfileResponse getProfileByUsername() {
        return mapper.toResponse(utils.getVerifiedFacultyProfile(SecurityUtils.getCurrentSecurityUser().getUsername()));
    }

    public Map<String, List<StudentProfileResponse>> getAllStudents() {
        FacultyProfile profile = utils.getVerifiedFacultyProfile(SecurityUtils.getCurrentSecurityUser().getUsername());
        Collection<FacultySubjectEnrollment> facultySubjectEnrollments =
                enrollmentRepo.findByFaculty_AuthUser_Username(profile.getAuthUsername());

        return facultySubjectEnrollments.stream().collect(Collectors.groupingBy(
                FacultySubjectEnrollment::getSubject,
                Collectors.mapping(e ->
                                mapper.toResponse(e.getStudent()),
                        Collectors.toList())
        ));
    }

    public Map<String, BigDecimal> getStudentMarksByStudentUsername(String studentUsername) {
        FacultyProfile faculty = utils.getVerifiedFacultyProfile(SecurityUtils.getCurrentSecurityUser().getUsername());
        utils.verifyFacultyTeachesStudent(faculty.getAuthUsername(), studentUsername);
        return studentRepo.findByAuthUser_Username(studentUsername)
                .orElseThrow(() -> new StudentNotFoundException(studentUsername))
                .getMarks();
    }

    @Transactional
    public void updateStudentMarks(String studentUsername, String subject, BigDecimal marks) {
        String facultyUsername = SecurityUtils.getCurrentSecurityUser().getUsername();
        boolean authorised = enrollmentRepo.existsByFaculty_AuthUser_UsernameAndStudent_AuthUser_UsernameAndSubject(
                facultyUsername, studentUsername, subject
        );
        if (!authorised)
            throw new AccessDeniedException("You are not authorized to grade: " + studentUsername + " on subject: " + subject);

        StudentProfile student = studentRepo.findByAuthUser_Username(studentUsername)
                .orElseThrow(() -> new StudentNotFoundException(studentUsername));

        student.getMarks().put(subject, marks);
        studentRepo.save(student);

    }
}
