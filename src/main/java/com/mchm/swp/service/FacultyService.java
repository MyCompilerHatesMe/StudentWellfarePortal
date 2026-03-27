package com.mchm.swp.service;

import com.mchm.swp.exception.FacultyNotFoundException;
import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.repo.FacultyProfileRepo;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import com.mchm.swp.security.SecurityUser;
import com.mchm.swp.utils.ProfileUtils;
import com.mchm.swp.utils.SecurityUtils;
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

    private final FacultyProfileRepo facultyRepo;
    private final FacultySubjectEnrollmentRepo facultySubjectEnrollmentRepo;
    private final StudentProfileRepo studentRepo;

    public FacultyProfileResponse getProfileByUsername() {
        return ProfileUtils.fromFacultyProfile(getVerifiedFacultyProfile());
    }

    public Map<String, List<StudentProfileResponse>> getAllStudents() {
        FacultyProfile profile = getVerifiedFacultyProfile();
        Collection<FacultySubjectEnrollment> facultySubjectEnrollments =
                facultySubjectEnrollmentRepo.findByFaculty_AuthUser_Username(profile.getAuthUsername());

        return facultySubjectEnrollments.stream().collect(Collectors.groupingBy(
                FacultySubjectEnrollment::getSubject,
                Collectors.mapping(e ->
                                ProfileUtils.fromStudentProfile(e.getStudent()),
                        Collectors.toList())
        ));
    }

    public Map<String, BigDecimal> getStudentMarksByStudentUsername(String studentUsername) {
        FacultyProfile faculty = getVerifiedFacultyProfile();
        verifyFacultyTeachesStudent(faculty.getAuthUsername(), studentUsername);
        return studentRepo.findByAuthUser_Username(studentUsername)
                .orElseThrow(() -> new StudentNotFoundException(studentUsername))
                .getMarks();
    }

    private void verifyFacultyTeachesStudent(String facultyUsername, String studentUsername) {
        boolean teaches = facultySubjectEnrollmentRepo.findByFaculty_AuthUser_Username(facultyUsername)
                .stream()
                .anyMatch(e -> e.getStudent().getAuthUsername().equals(studentUsername));
        if (!teaches) throw new AccessDeniedException("Faculty does not teach this student");
    }

    private FacultyProfile getVerifiedFacultyProfile() {
        SecurityUser user = SecurityUtils.getCurrentSecurityUser();
        if (!SecurityUtils.isFaculty(user)) throw new AccessDeniedException("Not faculty");
        return facultyRepo.findByAuthUser_Username(user.getUsername())
                .orElseThrow(() -> new FacultyNotFoundException(user.getUsername()));
    }
}
