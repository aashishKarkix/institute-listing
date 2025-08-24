package com.institute.listing.core.security;

import com.institute.listing.core.config.SecurityHeadersConfig;
import com.institute.listing.core.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final SecurityHeadersConfig securityHeadersConfig;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          SecurityHeadersConfig securityHeadersConfig) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.securityHeadersConfig = securityHeadersConfig;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //adding this for postman testing only
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/comparisons/**", "/login**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/logout").permitAll()
                )
                .headers(headers -> headers
                        .addHeaderWriter(securityHeadersConfig.getDefaultHeaders())
                );

        return http.build();
    }
}
