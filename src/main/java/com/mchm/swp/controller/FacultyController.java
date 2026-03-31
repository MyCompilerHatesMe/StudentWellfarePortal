package com.mchm.swp.controller;

import com.mchm.swp.model.dto.request.StandardProfileUpdateRequest;
import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.service.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('FACULTY')")
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService service;

    @GetMapping("/profile")
    public ResponseEntity<FacultyProfileResponse> getFacultyProfile() {
        return ResponseEntity.ok(service.getFacultyProfile());
    }

    @GetMapping("/students")
    public ResponseEntity<Map<String, List<StudentProfileResponse>>> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/student/{studentUsername}/marks")
    public ResponseEntity<Map<String, BigDecimal>> getMarksFromAuthUsername(@PathVariable String studentUsername) {
        return ResponseEntity.ok(service.getStudentMarksByStudentUsername(studentUsername));
    }

    @PutMapping("/student/{studentUsername}/marks/{subject}")
    public ResponseEntity<String> updateMarks(
            @PathVariable String studentUsername,
            @PathVariable String subject,
            @RequestBody BigDecimal marks
    ) {
        service.updateStudentMarks(studentUsername, subject, marks);
        return ResponseEntity.ok("Marks updated successfully");
    }

    @PutMapping("/profile")
    public ResponseEntity<FacultyProfileResponse> updateProfile(@RequestBody @Valid StandardProfileUpdateRequest request) {
        return ResponseEntity.ok(service.updateProfile(request));
    }
}
