package com.mchm.swp.service;

import com.mchm.swp.exception.ParentNotFoundException;
import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import com.mchm.swp.security.SecurityUser;
import com.mchm.swp.utils.ProfileUtils;
import com.mchm.swp.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentProfileRepo studentRepo;
    private final ParentProfileRepo parentRepo;
    private final FacultySubjectEnrollmentRepo facultySubjectEnrollmentRepo;

    public StudentProfileResponse getProfileByAuthUsername(String searchUsername) {
        return ProfileUtils.fromStudentProfile(getVerifiedStudentProfile(searchUsername));
    }

    public Map<String, BigDecimal> getMarksByUsername(String searchUsername) {
        return getVerifiedStudentProfile(searchUsername).getMarks();
    }

    public Map<String, FacultyProfileResponse> getFacultyByUsername(String searchUsername) {
        StudentProfile profile = getVerifiedStudentProfile(searchUsername);
        Collection<FacultySubjectEnrollment> facultySubjectEnrollments =
                facultySubjectEnrollmentRepo.findByStudent_AuthUser_Username(profile.getAuthUsername());

        return facultySubjectEnrollments.stream().collect(
                Collectors.toMap(FacultySubjectEnrollment::getSubject,
                        e -> ProfileUtils.fromFacultyProfile(e.getFaculty()))
        );
    }

    private StudentProfile getVerifiedStudentProfile(String searchUsername) {
        SecurityUser user = SecurityUtils.getCurrentSecurityUser();

        if (SecurityUtils.isStudent(user)) {
            if (!user.getUsername().equals(searchUsername))
                throw new AccessDeniedException("Usernames do not match");

            Optional<StudentProfile> optionalProfile = studentRepo.findByAuthUser_Username(searchUsername);
            if (optionalProfile.isEmpty())
                throw new StudentNotFoundException("Student with authUsername " + searchUsername + " not found");

            return optionalProfile.get();

        } else if (SecurityUtils.isParent(user)) {

            Optional<ParentProfile> optionalParentProfile = parentRepo.findByAuthUser_Username(user.getUsername());
            if (optionalParentProfile.isEmpty())
                throw new ParentNotFoundException("Parent with authUsername " + user.getUsername() + " not found");

            if (optionalParentProfile.get().getChildren().stream()
                    .map(StudentProfile::getAuthUsername)
                    .anyMatch(searchUsername::equals)) {
                Optional<StudentProfile> optionalStudentProfile = studentRepo.findByAuthUser_Username(searchUsername);
                if (optionalStudentProfile.isEmpty())
                    throw new StudentNotFoundException("Student with authUsername " + searchUsername + " not found");
                return optionalStudentProfile.get();

            } else throw new AccessDeniedException("Parent has no such child");

        } else {
            throw new AccessDeniedException("No valid role.");
        }
    }
}
