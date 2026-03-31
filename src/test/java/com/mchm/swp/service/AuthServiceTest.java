package com.mchm.swp.service;

import com.mchm.swp.exception.UsernameExistsException;
import com.mchm.swp.model.AuthUser;
import com.mchm.swp.model.dto.request.RegisterRequest;
import com.mchm.swp.repo.AuthUserRepo;
import com.mchm.swp.utils.DtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    // required for authService.
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private DtoMapper mapper;
    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private AuthUserRepo userRepo;
    @InjectMocks
    private AuthService authService;

    @Test
    void register_ShouldThrowException_WhenRoleIsAdmin() {
        RegisterRequest request = new RegisterRequest("hacker", "password123", Set.of("ROLE_ADMIN"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                authService.register(request));

        assertEquals("Invalid Role", exception.getMessage());
    }

    @Test
    void register_ShouldThrowException_WhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest("person", "password1234", Set.of("ROLE_FACULTY"));

        when(userRepo.save(any()))
                .thenReturn(new AuthUser())
                .thenThrow(DataIntegrityViolationException.class);

        authService.register(request);

        UsernameExistsException exception = assertThrows(UsernameExistsException.class, () ->
                authService.register(request));

        assertEquals("Username person already exists", exception.getMessage());
    }
}
