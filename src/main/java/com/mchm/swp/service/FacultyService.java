package com.mchm.swp.service;

import com.mchm.swp.model.dto.request.StandardProfileUpdateRequest;
import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultyProfileRepo;
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
    private final FacultyProfileRepo facultyRepo;
    private final ProfileUtils utils;
    private final DtoMapper mapper;

    public FacultyProfileResponse getFacultyProfile() {
        return mapper.toResponse(utils.getVerifiedFacultyProfile(SecurityUtils.getUsername()));
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
        return utils.getStudentProfile(studentUsername).getMarks();
    }

    @Transactional
    public void updateStudentMarks(String studentUsername, String subject, BigDecimal marks) {
        String facultyUsername = SecurityUtils.getUsername();
        boolean authorised = enrollmentRepo.existsByFaculty_AuthUser_UsernameAndStudent_AuthUser_UsernameAndSubject(
                facultyUsername, studentUsername, subject
        );
        if (!authorised)
            throw new AccessDeniedException("You are not authorized to grade: " + studentUsername + " on subject: " + subject);

        StudentProfile student = utils.getStudentProfile(studentUsername);

        student.getMarks().put(subject, marks);
        studentRepo.save(student);
    }

    @Transactional
    public FacultyProfileResponse updateProfile(StandardProfileUpdateRequest request) {
        FacultyProfile profile = utils.getVerifiedFacultyProfile(SecurityUtils.getUsername());

        if (!request.newName().isEmpty()) profile.setName(request.newName());
        if (!request.newEmail().isEmpty()) profile.setEmail(request.newEmail());

        return mapper.toResponse(facultyRepo.save(profile));
    }
}
