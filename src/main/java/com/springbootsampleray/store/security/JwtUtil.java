package com.springbootsampleray.store.security;

import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.JWTVerifier;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

//WORK FROM HERE

@Component
public class JwtUtil {
    @Value("${jwt.secret}") //read from application properties
    private String SECRET_KEY; 

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

    public String extractUsername(String token) //FIX ME!!!
    { 
        try
        {
            Algorithm algo = Algorithm.HMAC256(SECRET_KEY);

            JWTVerifier MyVerifier = JWT.require(algo)
                .withIssuer("Ray's Diary App")
                .acceptLeeway(1)
                .build();
            
                return MyVerifier.verify(token).getSubject(); 
        }
        catch(JWTVerificationException e)
        {
            System.err.println("Token verification failed: " + e.getMessage());
            return null;
        }
    }
}
