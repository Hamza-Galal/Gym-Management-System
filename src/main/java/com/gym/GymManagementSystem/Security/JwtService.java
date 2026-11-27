package com.gym.GymManagementSystem.Security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gym.GymManagementSystem.Model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
    @Value("${jwt.secret}")
    private  String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
        .subject(user.getEmail()).claim("role", user.getRole().name()).
        claim("tokenVersion", user.getTokenVersion()).
        issuedAt(now).
        expiration(expirationDate).
        signWith(getSigningKey()).compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build()
        .parseSignedClaims(token).getPayload();
    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }

    public Integer extractTokenVersion(String token){
        return extractAllClaims(token).get("tokenVersion",Integer.class);
    }

    public boolean isTokenValid(String token , User user){
        Claims claims = extractAllClaims(token);
        String email = claims.getSubject();
        Integer tokenVersion = claims.get("tokenVersion",Integer.class);    
        
        return email.equals(user.getEmail()) && tokenVersion != null && tokenVersion.equals(user.getTokenVersion()) 
        && claims.getExpiration().after(new Date());
    }

}
