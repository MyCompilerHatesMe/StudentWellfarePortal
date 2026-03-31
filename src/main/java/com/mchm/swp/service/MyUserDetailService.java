package com.mchm.swp.service;

import com.mchm.swp.repo.AuthUserRepo;
import com.mchm.swp.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private final AuthUserRepo userRepo;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(("User not found: " + username)));
    }
}
