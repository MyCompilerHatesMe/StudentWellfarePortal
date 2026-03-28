package com.mchm.swp.model.event;

import com.mchm.swp.model.AuthUser;

import java.util.Set;

public record UserRegisteredEvent(AuthUser user, Set<String> roles) {
}
