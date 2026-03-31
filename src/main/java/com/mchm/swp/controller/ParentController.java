package com.mchm.swp.controller;


import com.mchm.swp.model.dto.request.ParentProfileUpdateRequest;
import com.mchm.swp.model.dto.response.ParentProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/parent")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PARENT')")
public class ParentController {

    private final ParentService service;

    @GetMapping("/profile")
    public ResponseEntity<ParentProfileResponse> getProfile() {
        return ResponseEntity.ok(service.getProfile());
    }

    @GetMapping("/wards")
    public ResponseEntity<HashSet<StudentProfileResponse>> getWards() {
        return ResponseEntity.ok(service.getWards());
    }

    @GetMapping("/ward/{username}/profile")
    public ResponseEntity<StudentProfileResponse> getStudentProfile(@PathVariable String username) {
        return ResponseEntity.ok(service.getStudentProfile(username));
    }

    @GetMapping("/ward/{username}/marks")
    public ResponseEntity<Map<String, BigDecimal>> getStudentMarks(@PathVariable String username) {
        return ResponseEntity.ok(service.getStudentMarks(username));
    }

    @PutMapping("/profile")
    public ResponseEntity<ParentProfileResponse> updateProfile(
            @RequestBody @Valid ParentProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateProfile(request));
    }

}
