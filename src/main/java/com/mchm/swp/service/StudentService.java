package com.mchm.swp.service;

import com.mchm.swp.exception.ParentNotFoundException;
import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.model.dto.response.ProfileResponse;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import com.mchm.swp.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentProfileRepo studentRepo;
    private final ParentProfileRepo parentRepo;

    public ProfileResponse getProfileByAuthUsername(String searchUsername) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) auth.getPrincipal();
        if (authorityCheck("ROLE_STUDENT", user)) {
            if (!user.getUsername().equals(searchUsername))
                throw new AccessDeniedException("Usernames do not match");

            Optional<StudentProfile> prof = studentRepo.findByAuthUser_Username(searchUsername);
            if (prof.isEmpty())
                throw new StudentNotFoundException("Student with authUsername " + searchUsername + " not found");
            return fromStudentProfile(prof.get());

        } else if (authorityCheck("ROLE_PARENT", user)) {

            Optional<ParentProfile> profile = parentRepo.findByAuthUser_Username(user.getUsername());
            if (profile.isEmpty())
                throw new ParentNotFoundException("Parent with authUsername " + user.getUsername() + " not found");

            if (profile.get().getChildren().stream()
                    .map(StudentProfile::getAuthUsername)
                    .anyMatch(searchUsername::equals)) {
                Optional<StudentProfile> prof = studentRepo.findByAuthUser_Username(searchUsername);
                if (prof.isEmpty())
                    throw new StudentNotFoundException("Student with authUsername " + searchUsername + " not found");
                return fromStudentProfile(prof.get());
            } else throw new AccessDeniedException("Parent has no such child");

        } else {
            throw new AccessDeniedException("No valid role.");
        }
    }


    private boolean authorityCheck(String role, SecurityUser user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }

    private ProfileResponse fromStudentProfile(StudentProfile profile) {
        return ProfileResponse.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .rollNo(profile.getRollNo())
                .build();
    }

}
