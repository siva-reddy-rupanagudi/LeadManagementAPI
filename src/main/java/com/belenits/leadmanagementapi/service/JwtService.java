package com.belenits.leadmanagementapi.service;

import com.belenits.leadmanagementapi.repo.CounsellorRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    private static final Duration TOKEN_VALIDITY = Duration.ofHours(30);

    @Autowired
    private CounsellorRepo counsellorRepo;

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateKey(String counsellorId) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("counsellorId", counsellorId);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(counsellorRepo.findById(counsellorId).get().getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY.toMillis()))
                .and()
                .signWith(getKey())
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractCounsellorId(String token) {
        return extractClaim(token, claims -> claims.get("counsellorId", String.class));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUserName(token);
        System.out.println("JWT Username : " + username);
        System.out.println("DB Username  : " + userDetails.getUsername());
        System.out.println("Expired      : " + isTokenExpired(token));

        boolean valid = username.equals(userDetails.getUsername())
                && !isTokenExpired(token);

        System.out.println("Token Valid = " + valid);
        return valid;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
