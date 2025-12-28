package com.institute.listing.core.security.handler;

import com.institute.listing.core.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    public OAuth2AuthenticationSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        assert user != null;
        String email = user.getAttribute("email");

        String token = jwtService.generateAccessToken(email, authentication.getAuthorities());
        String refreshToken = jwtService.generateRefreshToken(email);

        Cookie jwtCookie = new Cookie("access_token", token);
        jwtCookie.setHttpOnly(true); //cannot access by JavaScript
        jwtCookie.setSecure(false);  //https --> true
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(15 * 60); //15min

        response.addCookie(jwtCookie);

        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        response.sendRedirect("/");
    }
}
