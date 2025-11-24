package com.medilabosolutions.gateway_service.config;

//import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String SECRET = "A5f89G2kLmNqP0Rs!TuvWxYz12345678"; // même clé que le back

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

