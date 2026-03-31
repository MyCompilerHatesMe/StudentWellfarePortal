package com.mchm.swp.utils;

import com.mchm.swp.model.Role;
import com.mchm.swp.security.SecurityUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static boolean hasRole(SecurityUser user, String role) {
        return user.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    public static boolean isAdmin(SecurityUser user) {
        return hasRole(user, Role.ROLE_ADMIN.name());
    }

    public static boolean isStudent(SecurityUser user) {
        return hasRole(user, Role.ROLE_STUDENT.name());
    }

    public static boolean isFaculty(SecurityUser user) {
        return hasRole(user, Role.ROLE_FACULTY.name());
    }

    public static boolean isParent(SecurityUser user) {
        return hasRole(user, Role.ROLE_PARENT.name());
    }

    public static SecurityUser getCurrentSecurityUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getUsername() {
        return getCurrentSecurityUser().getUsername();
    }
}