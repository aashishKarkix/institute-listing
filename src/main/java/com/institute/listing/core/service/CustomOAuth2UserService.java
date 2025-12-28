package com.institute.listing.core.service;

import com.institute.listing.core.exception.NotFoundException;
import com.institute.listing.core.model.Role;
import com.institute.listing.core.model.User;
import com.institute.listing.core.repository.RoleRepository;
import com.institute.listing.core.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauthUser = super.loadUser(userRequest);

        String googleId = oauthUser.getAttribute("sub");
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new NotFoundException("Default USER role not found"));

            User newUser = User.builder()
                    .name(name)
                    .email(email)
                    .googleId(googleId)
                    .roles(new HashSet<>())
                    .createdAt(LocalDateTime.now())
                    .isActive(true)
                    .build();

            newUser.getRoles().add(defaultRole);
            return userRepository.save(newUser);
        });

        if (user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new NotFoundException("Default USER role not found"));
            user.getRoles().add(defaultRole);
            userRepository.save(user);
        }

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        return new DefaultOAuth2User(authorities, oauthUser.getAttributes(), "sub");
    }
}
