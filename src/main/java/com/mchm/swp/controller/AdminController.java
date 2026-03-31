package com.mchm.swp.controller;

import com.mchm.swp.model.dto.request.EnrollmentConnectionRequest;
import com.mchm.swp.model.dto.request.WardConnectionRequest;
import com.mchm.swp.model.dto.response.EnrollmentConnectionResponse;
import com.mchm.swp.model.dto.response.WardConnectionResponse;
import com.mchm.swp.service.AdminService;
import jakarta.validation.Valid;
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
    public ResponseEntity<EnrollmentConnectionResponse> connectStudentFaculty(@RequestBody @Valid EnrollmentConnectionRequest req) {
        return ResponseEntity.ok(service.enroll(
                req.studentUsername(),
                req.facultyUsername(),
                req.subject())
        );
    }

    @PostMapping("/connect")
    public ResponseEntity<WardConnectionResponse> connectParentWard(@RequestBody @Valid WardConnectionRequest req) {
        return ResponseEntity.ok(service.connect(
                req.parentUsername(),
                req.studentUsername()
        ));
    }

}
