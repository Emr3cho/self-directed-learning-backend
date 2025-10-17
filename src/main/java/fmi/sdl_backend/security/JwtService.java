package fmi.sdl_backend.security;

import fmi.sdl_backend.presistance.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public User getUserFromToken(String token) {
        Claims claims = decodeToken(token);
        String email = claims.get("email", String.class);
        String fullName = claims.get("name", String.class);

        return new User(email, fullName);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = getUserFromToken(token).getEmail();
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        Claims claims = decodeToken(token);
        Long expireDateAsLong = claims.get("exp", Long.class);
        return new Date(expireDateAsLong);
    }
}
