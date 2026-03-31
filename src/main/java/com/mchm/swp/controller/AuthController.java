package com.mchm.swp.controller;

import com.mchm.swp.model.dto.request.LoginRequest;
import com.mchm.swp.model.dto.request.RegisterRequest;
import com.mchm.swp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    // TODO: remove later
    @GetMapping("/helloThere")
    public ResponseEntity<String> test () {
        return ResponseEntity.ok("General Kenobi");
    }
}
