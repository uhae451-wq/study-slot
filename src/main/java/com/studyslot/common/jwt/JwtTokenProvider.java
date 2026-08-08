package com.studyslot.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis
    ) {
        // 문자열 비밀키를 서명에 쓸 수 있는 SecretKey 객체로 변환
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMillis = expirationMillis;
    }

    // 토큰 발급: userId, email을 담아서 서명된 토큰 문자열 생성
    public String createToken(Long userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(String.valueOf(userId))   // 토큰의 주인이 누구인지 (보통 식별자)
                .claim("email", email)             // 추가 정보는 claim으로 담음
                .issuedAt(now)                     // 발급 시각
                .expiration(expiry)                // 만료 시각
                .signWith(secretKey)                // 비밀키로 서명
                .compact();
    }

    // 토큰이 유효한지 검증 (서명 위조 여부, 만료 여부 등)
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // 서명이 안 맞거나, 만료됐거나, 형식이 깨졌거나 등등 전부 여기로 옴
            return false;
        }
    }

    // 토큰에서 userId 꺼내기
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    // 토큰에서 email 꺼내기
    public String getEmail(String token) {
        Claims claims = parseClaims(token);
        return claims.get("email", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}