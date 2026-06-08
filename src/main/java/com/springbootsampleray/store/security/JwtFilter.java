package com.springbootsampleray.store.security;

import java.io.IOException;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springbootsampleray.store.user.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.String;

@Component
public class JwtFilter extends OncePerRequestFilter{
    @Autowired
    private JwtUtil jwtUtil; 
    @Autowired
    private UserService service; 

    @Override
    protected void doFilterInternal(HttpServletRequest req, 
        HttpServletResponse response,
         FilterChain chain) throws ServletException, IOException
    {
        String authHeader = req.getHeader("Authorization"); 

        if (authHeader.startsWith("Bearer"))
        {
            String token_raw = authHeader.substring(6); 
            if (jwtUtil.isTokenValid(token_raw))
            {
                String username = jwtUtil.extractUsername(token_raw); //FIX ME!!!
                logger.info("Successfully authenticated user" + authHeader); 
                chain.doFilter(req, response);
            }
        }
    }
}
