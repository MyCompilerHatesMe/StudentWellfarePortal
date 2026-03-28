package com.mchm.swp.model.event;

import com.mchm.swp.model.AuthUser;
import com.mchm.swp.model.Role;

import java.util.Set;

public record UserRegisteredEvent(AuthUser user, Set<Role> roles) {
}
