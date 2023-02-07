package com.example.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@UtilityClass
@Slf4j
public class JwtUtil {

    private final String secret = "AF1WqAR3Akw2Y9IEkJF+H4eALCQeLA6VjgcoU8THcGCN7dSGytnLDqSbEgEGucVdkS7jOGqi65NmH91Dwa/yQewgHPsaPwQOVUXhwA2Npl4QoP4VY1XTH1M7mkulJpU/vc1Z9AOZx6PiP2JqW5P/+CSNt+w26CFNzVBGECYx1cqJfsbiwOYYiOM3+l+s5PpPm8LP28DzjsgC2FBJkW0qtuNyCTOQawRgJZXstg==";
    private final long tokenValidity = 18000;

    public String buildJwt(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + tokenValidity;
        Date exp = new Date(expMillis);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Claims getClaims(final String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Unable to get claims.", e);
        }
        return null;
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
