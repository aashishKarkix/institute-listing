package com.institute.listing.core.util;

import com.institute.listing.core.exception.NotFoundException;
import com.institute.listing.core.model.User;
import com.institute.listing.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;

    public User getAuthenticatedUser(OAuth2User oauthUser) {
        if (oauthUser == null) throw new AccessDeniedException("User is not authenticated");

        String email = oauthUser.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new AccessDeniedException("OAuth user email is missing");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
