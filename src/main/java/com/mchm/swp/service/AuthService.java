package com.mchm.swp.service;

import com.mchm.swp.model.AuthUser;
import com.mchm.swp.model.Role;
import com.mchm.swp.model.dto.request.LoginRequest;
import com.mchm.swp.model.dto.request.RegisterRequest;
import com.mchm.swp.model.dto.response.RegisterResponse;
import com.mchm.swp.model.event.UserRegisteredEvent;
import com.mchm.swp.repo.AuthUserRepo;
import com.mchm.swp.security.SecurityUser;
import com.mchm.swp.utils.DtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final DtoMapper mapper;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public RegisterResponse register (RegisterRequest request) throws IllegalArgumentException {
        AuthUser user = new AuthUser();

        user.setRoles(
                request.getRoles().stream()
                        .map(s -> {
                            if (s.equalsIgnoreCase(Role.ROLE_ADMIN.name()))
                                throw new IllegalArgumentException("Invalid Role");
                            return Role.valueOf(s);
                        })
                        .collect(Collectors.toSet())
        );

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getName());

        AuthUser saved = userRepo.save(user);

        publisher.publishEvent(new UserRegisteredEvent(saved, saved.getRoles()));

        return mapper.toResponse(saved);
    }

    public String login (LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword())
            );
            return jwtService.generateToken(
                    request.getName(),
                    // authUser will never be null, auth guarantees a "fully authenticated object"
                    // or AuthenticationException.
                    ((SecurityUser) auth.getPrincipal()).authUser().getRoles()
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid Credentials");
        }
    }
}
