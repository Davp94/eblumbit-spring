package com.blumbit.eblumbit.auth.services;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.entities.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {

    @Value("${application.jwt.secret-key}")
    private String secretKey;

    @Value("${application.jwt.expiration}")
    private Long expiration;

    @Value("${application.jwt.refresh-expiration}")
    private Long refreshExpiration;

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String buildToken(Usuario usuario, Long expiration) {
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claims(Map.of("_uid", usuario.getId()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateAccesToken(Usuario usuario) {
        return buildToken(usuario, expiration);
    }

    public String generateRefreshToken(Usuario usuario) {
        return buildToken(usuario, refreshExpiration);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, Usuario usuario) {
        String username = extractUsername(token);
        return username.equals(usuario.getUsername()) && !isTokenExpired(token);
    }

    private SecretKey getSigningKey() {
        byte[] keyBites = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBites);
    }
}
