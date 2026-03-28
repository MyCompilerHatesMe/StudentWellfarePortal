package com.mchm.swp.utils;

import com.mchm.swp.model.AuthUser;
import com.mchm.swp.model.dto.response.ConnectionResponse;
import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.RegisterResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.StudentProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DtoMapper {
    StudentProfileResponse toResponse(StudentProfile profile);

    FacultyProfileResponse toResponse(FacultyProfile profile);

    @Mapping(source = "faculty.authUser.username", target = "facultyUsername")
    @Mapping(source = "student.authUser.username", target = "studentUsername")
    ConnectionResponse toResponse(FacultySubjectEnrollment enrollment);

    @Mapping(source = "username", target = "user")
    RegisterResponse toResponse(AuthUser user);
}