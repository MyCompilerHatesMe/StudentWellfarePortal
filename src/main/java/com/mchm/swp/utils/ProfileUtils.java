package com.mchm.swp.utils;

import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.StudentProfile;

public class ProfileUtils {
    private ProfileUtils() {
    }

    public static StudentProfileResponse fromStudentProfile(StudentProfile profile) {
        return StudentProfileResponse.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .rollNo(profile.getRollNo())
                .build();
    }

    public static FacultyProfileResponse fromFacultyProfile(FacultyProfile profile) {
        return FacultyProfileResponse.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .build();
    }
}
