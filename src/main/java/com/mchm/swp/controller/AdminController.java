package com.mchm.swp.controller;

import com.mchm.swp.model.dto.request.ConnectionRequest;
import com.mchm.swp.model.dto.response.ConnectionResponse;
import com.mchm.swp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    @PostMapping("/enroll")
    public ResponseEntity<ConnectionResponse> connectStudentFaculty(@RequestBody ConnectionRequest req) {
        return ResponseEntity.ok(service.connect(
                req.getStudentUsername(),
                req.getFacultyUsername(),
                req.getSubject())
        );
    }
}
