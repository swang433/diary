package com.springbootsampleray.store.security;

import com.springbootsampleray.store.user.UserRepo;
import java.util.Date;
import java.time.Instant;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "there are consequences for killing a god";

    public String generateToken(String Username)
    {
        //define token lifespan
        long expirationTimeMS = 900 * 1000; 
        Date now = new Date(); //generates current system time
        Date expireDate = new Date(now.getTime() + expirationTimeMS); 

        //select encrpytion algorithm
        Algorithm algo = Algorithm.HMAC256(SECRET_KEY);

        //build, sign, and return token
        return JWT.create()
                .withSubject(Username)
                .withIssuer("Ray's Diary App")
                .withIssuedAt(now)
                .withExpiresAt(expireDate)
                .sign(algo); 
    }

    public String extractUsername(String token)
    {
        return "Username extraction placeholder"; 
    }

    public boolean isTokenValid(String token)
    {
        return false; 
    }
}
