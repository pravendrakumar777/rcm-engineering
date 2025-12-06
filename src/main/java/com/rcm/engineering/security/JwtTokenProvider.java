package com.rcm.engineering.security;
/*
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration-ms}")
    private long validityInMilliseconds;
    @Value("${jwt.header:Authorization}")
    private String header;
    @Value("${jwt.prefix:Bearer }")
    private String tokenPrefix;
    private Key key;

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String username, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        if (roles != null && !roles.isEmpty()) {
            claims.put("roles", roles.stream().collect(Collectors.toList()));
        }

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Set<String> getRoles(String token) {
        Object roles = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles");

        if (roles instanceof Collection) {
            return ((Collection<?>) roles).stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public String resolveTokenFromHeader(String headerValue) {
        if (headerValue == null) return null;
        if (headerValue.startsWith(tokenPrefix)) {
            return headerValue.substring(tokenPrefix.length()).trim();
        }
        return null;
    }
}

 */
