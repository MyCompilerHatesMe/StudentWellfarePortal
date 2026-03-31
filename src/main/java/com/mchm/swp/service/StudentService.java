package com.mchm.swp.service;

import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.model.dto.request.StandardProfileUpdateRequest;
import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import com.mchm.swp.utils.DtoMapper;
import com.mchm.swp.utils.ProfileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final FacultySubjectEnrollmentRepo facultySubjectEnrollmentRepo;
    private final StudentProfileRepo studentRepo;
    private final ProfileUtils utils;
    private final DtoMapper mapper;

    public StudentProfileResponse getProfileByAuthUsername(String searchUsername) {
        return mapper.toResponse(utils.getVerifiedStudentProfile(searchUsername));
    }

    public Map<String, BigDecimal> getMarksByUsername(String searchUsername) {
        return utils.getVerifiedStudentProfile(searchUsername).getMarks();
    }

    public Map<String, FacultyProfileResponse> getFacultyByUsername(String searchUsername) {
        StudentProfile profile = utils.getVerifiedStudentProfile(searchUsername);
        Collection<FacultySubjectEnrollment> facultySubjectEnrollments =
                facultySubjectEnrollmentRepo.findByStudent_AuthUser_Username(profile.getAuthUsername());

        return facultySubjectEnrollments.stream().collect(
                Collectors.toMap(FacultySubjectEnrollment::getSubject,
                        e -> mapper.toResponse(e.getFaculty()))
        );
    }

    public StudentProfileResponse updateStudentByUsername(String username,
                                                          StandardProfileUpdateRequest request) {

        StudentProfile profile = studentRepo.findByAuthUser_Username(username)
                .orElseThrow(() -> new StudentNotFoundException(username));

        if (!request.newName().isEmpty()) profile.setName(request.newName());
        if (!request.newEmail().isEmpty()) profile.setEmail(request.newEmail());

        return mapper.toResponse(studentRepo.save(profile));
    }



}
