package com.mchm.swp.utils;

import com.mchm.swp.exception.FacultyNotFoundException;
import com.mchm.swp.exception.ParentNotFoundException;
import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultyProfileRepo;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import com.mchm.swp.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileUtils {

    private final StudentProfileRepo studentRepo;
    private final FacultyProfileRepo facultyRepo;
    private final FacultySubjectEnrollmentRepo enrollmentRepo;
    private final ParentProfileRepo parentRepo;

    public StudentProfile getVerifiedStudentProfile(String searchUsername) {
        SecurityUser user = SecurityUtils.getCurrentSecurityUser();

        if (SecurityUtils.isAdmin(user)) return getStudentProfile(searchUsername);
        if (SecurityUtils.isStudent(user)) {
            if (!user.getUsername().equals(searchUsername))
                throw new AccessDeniedException("Usernames do not match");

            return getStudentProfile(searchUsername);

        } else if (SecurityUtils.isParent(user)) {

            ParentProfile parentProfile = getParentProfile(user.getUsername());

            if (parentProfile.getChildren().stream()
                    .map(StudentProfile::getAuthUsername)
                    .noneMatch(searchUsername::equals)) {
                throw new AccessDeniedException("Parent has no such child");
            }

            return getStudentProfile(searchUsername);
        } else {
            throw new AccessDeniedException("No valid role.");
        }
    }

    public StudentProfile getStudentProfile(String searchUsername) {
        return studentRepo.findByAuthUser_Username(searchUsername)
                .orElseThrow(() -> new StudentNotFoundException(searchUsername));
    }

    public ParentProfile getParentProfile(String searchUsername) {
        return parentRepo.findByAuthUser_Username(searchUsername)
                .orElseThrow(() -> new ParentNotFoundException(searchUsername));
    }

    public FacultyProfile getFacultyProfile(String searchUsername) {
        return facultyRepo.findByAuthUser_Username(searchUsername)
                .orElseThrow(() -> new FacultyNotFoundException(searchUsername));
    }

    public void verifyFacultyTeachesStudent(String facultyUsername, String studentUsername) {
        boolean teaches = enrollmentRepo.findByFaculty_AuthUser_Username(facultyUsername)
                .stream()
                .anyMatch(e -> e.getStudent().getAuthUsername().equals(studentUsername));
        if (!teaches) throw new AccessDeniedException("Faculty does not teach this student");
    }

    public FacultyProfile getVerifiedFacultyProfile(String searchUsername) {
        SecurityUser user = SecurityUtils.getCurrentSecurityUser();
        if (!searchUsername.equals(user.getUsername()) && !SecurityUtils.isAdmin(user))
            throw new AccessDeniedException("You can only check your own profile");
        return getFacultyProfile(searchUsername);
    }

}
