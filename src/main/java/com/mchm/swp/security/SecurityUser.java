package com.mchm.swp.security;

import com.mchm.swp.model.AuthUser;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record SecurityUser (AuthUser authUser) implements UserDetails {

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return authUser.getPasswordHash();
    }

    @Override
    @NullMarked
    public String getUsername () {
        return authUser.getUsername();
    }
}