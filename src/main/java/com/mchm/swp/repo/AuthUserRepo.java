package com.mchm.swp.repo;

import com.mchm.swp.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepo extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername (String username);
}
