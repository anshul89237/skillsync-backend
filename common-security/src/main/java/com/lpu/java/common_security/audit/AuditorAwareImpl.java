package com.lpu.java.common_security.audit;

import com.lpu.java.common_security.config.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("SYSTEM");
        }

        if (authentication.getPrincipal() instanceof UserPrincipal) {
            return Optional.of(((UserPrincipal) authentication.getPrincipal()).getEmail());
        }

        return Optional.of(authentication.getName());
    }
}
