package com.institute.listing.core.controller;

import com.institute.listing.core.model.User;
import com.institute.listing.core.security.annotation.Admin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestLoginController {

    @GetMapping("/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String name = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User user) {
                name = user.getName();
            } else if (principal instanceof String email) {
                name = email;
            } else if (principal instanceof OAuth2User oauth2User) {
                name = oauth2User.getAttribute("name");
            }
        }

        return """
            <html>
                <body>
                    <h1>Welcome to Institute Listing</h1>
                    %s
                    <br/>
                    <a href='/oauth2/authorization/google'>Login with Google</a>
                </body>
            </html>
        """.formatted(name != null ? "<p>Hello, " + name + "</p>" : "<p>Hello, Guest!</p>");
    }

    @com.institute.listing.core.security.annotation.User
    @GetMapping("/user")
    public String user() {
        return "Test User endpoint - accessible after login";
    }

    @Admin
    @GetMapping("/admin")
    public String admin() {
        return "Test Admin endpoint - accessible after login";
    }
}
