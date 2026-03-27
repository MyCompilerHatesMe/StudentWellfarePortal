package com.mchm.swp.controller;

import com.mchm.swp.model.dto.response.FacultyProfileResponse;
import com.mchm.swp.model.dto.response.StudentProfileResponse;
import com.mchm.swp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('PARENT', 'STUDENT')")
public class StudentController {

    private final StudentService service;

    @GetMapping("/{authUsername}/profile")
    ResponseEntity<StudentProfileResponse> getProfileByUsername(@PathVariable String authUsername) {
        return ResponseEntity.ok(service.getProfileByAuthUsername(authUsername));
    }

    @GetMapping("/{authUsername}/marks")
    ResponseEntity<Map<String, BigDecimal>> getMarksByUsername(@PathVariable String authUsername) {
        return ResponseEntity.ok(service.getMarksByUsername(authUsername));
    }

    @GetMapping("/{authUsername}/faculty")
    ResponseEntity<Map<String, FacultyProfileResponse>> getFacultyByUsername(@PathVariable String authUsername) {
        return ResponseEntity.ok(service.getFacultyByUsername(authUsername));
    }

}
