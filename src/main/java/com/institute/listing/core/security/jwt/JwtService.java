package com.institute.listing.core.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class JwtService {

    private static final String SECRET =
            KeyGenerator256Bit.generateDynamicSecretKey();

    private static final SecretKey SIGNING_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private static final long ACCESS_TOKEN_EXPIRATION_MS = 60 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000;


    public String generateAccessToken(String email, Collection<? extends GrantedAuthority> authorities) {
        return generateToken(email, authorities, ACCESS_TOKEN_EXPIRATION_MS);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, List.of(), REFRESH_TOKEN_EXPIRATION_MS);
    }

    private String generateToken(String email, Collection<? extends GrantedAuthority> authorities, long expiration) {

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(r -> r.startsWith("ROLE_"))
                .toList();

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(SIGNING_KEY)
                .compact();
    }


    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SIGNING_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }
}
