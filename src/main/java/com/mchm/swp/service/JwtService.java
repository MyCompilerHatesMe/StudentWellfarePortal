package com.mchm.swp.service;

import com.mchm.swp.model.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private static final String SECRET_KEY = "this-is-a-secret-key-it-is-long-";

    private SecretKey getSigningKey () {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken (String name, Set<Role> roles) {
        return Jwts.builder()
                .subject(name)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) //24 hours
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername (String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @SuppressWarnings("unchecked") // we know for a fact that jjwt gives back a List<String> at least
    public Set<Role> extractRoles (String token) {
        Set<String> roles = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", Set.class);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }

    public boolean isTokenValid (String token) {
        try {
            extractUsername(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
