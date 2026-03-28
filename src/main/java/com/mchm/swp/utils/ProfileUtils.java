package com.mchm.swp.utils;

import com.mchm.swp.exception.FacultyNotFoundException;
import com.mchm.swp.exception.ParentNotFoundException;
import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.model.AuthUser;
import com.mchm.swp.model.event.UserRegisteredEvent;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultyProfileRepo;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import com.mchm.swp.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileUtils {

    private final StudentProfileRepo studentRepo;
    private final FacultyProfileRepo facultyRepo;
    private final FacultySubjectEnrollmentRepo enrollmentRepo;
    private final ParentProfileRepo parentRepo;

    public StudentProfile getVerifiedStudentProfile(String searchUsername) {
        SecurityUser user = SecurityUtils.getCurrentSecurityUser();

        if (SecurityUtils.isStudent(user)) {
            if (!user.getUsername().equals(searchUsername))
                throw new AccessDeniedException("Usernames do not match");

            return getProfile(searchUsername);

        } else if (SecurityUtils.isParent(user)) {

            ParentProfile parentProfile = parentRepo.findByAuthUser_Username(user.getUsername())
                    .orElseThrow(() -> new ParentNotFoundException(user.getUsername()));

            if (parentProfile.getChildren().stream()
                    .map(StudentProfile::getAuthUsername)
                    .noneMatch(searchUsername::equals)) {
                throw new AccessDeniedException("Parent has no such child");
            }

            return getProfile(searchUsername);
        } else {
            throw new AccessDeniedException("No valid role.");
        }
    }

    public StudentProfile getProfile(String searchUsername) {
        return studentRepo.findByAuthUser_Username(searchUsername)
                .orElseThrow(() -> new StudentNotFoundException(searchUsername));
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
        return facultyRepo.findByAuthUser_Username(searchUsername)
                .orElseThrow(() -> new FacultyNotFoundException(searchUsername));
    }

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        AuthUser user = event.user();
        Set<String> roles = event.roles();

        if (roles.contains("ROLE_STUDENT")) {
            StudentProfile profile = new StudentProfile();
            profile.setAuthUser(user);
            profile.setName(user.getUsername());
            profile.setEmail(user.getUsername() + "@university.edu.in");
            studentRepo.save(profile);
        }

    }
}
