package com.mchm.swp.service;


import com.mchm.swp.model.AuthUser;
import com.mchm.swp.model.Role;
import com.mchm.swp.model.event.UserRegisteredEvent;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultyProfileRepo;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.repo.StudentProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final StudentProfileRepo studentRepo;
    private final FacultyProfileRepo facultyRepo;
    private final ParentProfileRepo parentRepo;

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        AuthUser user = event.user();
        Set<Role> roles = event.roles();

        if (roles.contains(Role.ROLE_STUDENT))
            studentRepo.save(createStudentProfile(user));
        if (roles.contains(Role.ROLE_FACULTY))
            facultyRepo.save(createFacultyProfile(user));
        if (roles.contains(Role.ROLE_PARENT))
            parentRepo.save(createParentProfile(user));
    }

    private StudentProfile createStudentProfile(AuthUser user) {
        StudentProfile profile = new StudentProfile();
        profile.setAuthUser(user);
        profile.setName(user.getUsername());
        profile.setRollNo("S" + String.format("%06d", studentRepo.count()));
        profile.setEmail(user.getUsername() + "@university.edu.in");
        return profile;
    }

    private FacultyProfile createFacultyProfile(AuthUser user) {
        FacultyProfile profile = new FacultyProfile();
        profile.setAuthUser(user);
        profile.setName(user.getUsername());
        profile.setEmail(user.getUsername() + "@university.edu.in");
        return profile;
    }

    private ParentProfile createParentProfile(AuthUser user) {
        ParentProfile profile = new ParentProfile();
        profile.setAuthUser(user);
        profile.setName(user.getUsername());
        profile.setEmail("placeholder");
        profile.setNumber("placeholder"); // should be changed after registration.
        return profile;
    }
}
