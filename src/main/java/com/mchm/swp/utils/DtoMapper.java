package com.mchm.swp.utils;

import com.mchm.swp.model.AuthUser;
import com.mchm.swp.model.dto.response.*;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.model.profiles.StudentProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DtoMapper {
    StudentProfileResponse toResponse(StudentProfile profile);

    ParentProfileResponse toResponse(ParentProfile profile);

    FacultyProfileResponse toResponse(FacultyProfile profile);

    @Mapping(source = "faculty.authUser.username", target = "facultyUsername")
    @Mapping(source = "student.authUser.username", target = "studentUsername")
    ConnectionResponse toResponse(FacultySubjectEnrollment enrollment);

    @Mapping(source = "username", target = "user")
    RegisterResponse toResponse(AuthUser user);

}