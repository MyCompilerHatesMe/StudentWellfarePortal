package com.mchm.swp.service;


import com.mchm.swp.model.dto.request.ParentProfileUpdateRequest;
import com.mchm.swp.model.dto.response.ParentProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.model.profiles.ParentProfile;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.utils.DtoMapper;
import com.mchm.swp.utils.ProfileUtils;
import com.mchm.swp.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentService {
    private final ParentProfileRepo parentRepo;
    private final StudentService studentService;
    private final ProfileUtils utils;
    private final DtoMapper mapper;

    public ParentProfileResponse getProfile() {
        String username = SecurityUtils.getCurrentSecurityUser().getUsername();
        return mapper.toResponse(utils.getParentProfile(username));
    }

    public HashSet<StudentProfileResponse> getWards() {
        ParentProfile profile = utils.getParentProfile(SecurityUtils.getCurrentSecurityUser().getUsername());

        return profile.getChildren()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public StudentProfileResponse getStudentProfile(String searchUsername) {
        return studentService.getProfileByAuthUsername(searchUsername);
    }

    public Map<String, BigDecimal> getStudentMarks(String searchUsername) {
        return studentService.getMarksByUsername(searchUsername);
    }

    public ParentProfileResponse updateProfile(ParentProfileUpdateRequest request) {
        ParentProfile profile = utils.getParentProfile(SecurityUtils.getUsername());

        if (!request.newName().isEmpty()) profile.setName(request.newName());
        if (!request.newEmail().isEmpty()) profile.setEmail(request.newEmail());
        if (!request.newNumber().isEmpty()) profile.setNumber(request.newNumber());

        return mapper.toResponse(parentRepo.save(profile));
    }
}

