package com.mchm.swp.controller;

import com.mchm.swp.exception.ParentNotFoundException;
import com.mchm.swp.exception.StudentNotFoundException;
import com.mchm.swp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping("/{authUsername}/profile")
    ResponseEntity<?> getProfileByUsername(@PathVariable String authUsername) {
        try {
            return ResponseEntity.ok(service.getProfileByAuthUsername(authUsername));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (StudentNotFoundException | ParentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            //TODO: THIS IS FOR TESTING. REMOVE THIS LATER
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getStackTrace());
        }
    }
}
