package com.institute.listing.core.security;

import com.institute.listing.core.config.SecurityHeadersConfig;
import com.institute.listing.core.security.handler.CustomLogoutHandler;
import com.institute.listing.core.security.jwt.JwtAuthenticationFilter;
import com.institute.listing.core.security.jwt.JwtService;
import com.institute.listing.core.security.handler.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler successHandler;
    private final CustomLogoutHandler customLogoutHandler;
    private final JwtService jwtService;
    private final SecurityHeadersConfig securityHeadersConfig;

    public SecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler successHandler, CustomLogoutHandler customLogoutHandler,
            JwtService jwtService,
            SecurityHeadersConfig securityHeadersConfig
    ) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.successHandler = successHandler;
        this.customLogoutHandler = customLogoutHandler;
        this.jwtService = jwtService;
        this.securityHeadersConfig = securityHeadersConfig;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository
    cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .securityContext(securityContext ->
                        securityContext.securityContextRepository(
                                new NullSecurityContextRepository()
                        )
                )

                .requestCache(RequestCacheConfigurer::disable)

                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(restAuthenticationEntryPoint())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/logout",
                                "/oauth2/**",
                                "/login**",
                                "/error",
                                "/api/comparisons/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler((request, response, authentication)
                                -> response.sendRedirect("/"))
                        .permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(auth ->
                                auth.authorizationRequestRepository(
                                        cookieAuthorizationRequestRepository()
                                )
                        )
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)
                        )
                        .successHandler(successHandler)
                )

                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                .headers(headers ->
                        headers.addHeaderWriter(
                                securityHeadersConfig.getDefaultHeaders()
                        )
                );

        return http.build();
    }
}
