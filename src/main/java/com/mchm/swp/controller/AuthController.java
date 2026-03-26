package com.mchm.swp.controller;

import com.mchm.swp.model.dto.request.LoginRequest;
import com.mchm.swp.model.dto.request.RegisterRequest;
import com.mchm.swp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(422).body("Invalid role");
        }
        catch (Exception e) {
            return ResponseEntity.status(409).body("Error registering user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(service.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // remove later
    @GetMapping("/helloThere")
    public ResponseEntity<String> test () {
        return ResponseEntity.ok("General Kenobi");
    }
}
