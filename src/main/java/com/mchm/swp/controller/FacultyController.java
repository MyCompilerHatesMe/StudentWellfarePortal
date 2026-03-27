package com.mchm.swp.controller;

import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<FacultyProfileResponse> getProfileByUsername() {
        return ResponseEntity.ok(service.getProfileByUsername());
    }

    @GetMapping("/students")
    public ResponseEntity<Map<String, List<StudentProfileResponse>>> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/student/{studentUsername}/marks")
    public ResponseEntity<Map<String, BigDecimal>> getMarksFromAuthUsername(@PathVariable String studentUsername) {
        return ResponseEntity.ok(service.getStudentMarksByStudentUsername(studentUsername));
    }
}
