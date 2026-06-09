package com.springbootsampleray.store.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtFilter extends OncePerRequestFilter{
    @Autowired
    private JwtUtil jwtUtil; 
    @Autowired
    private UserDetailsService Uservice; 

    @Override
    protected void doFilterInternal(HttpServletRequest req, 
        HttpServletResponse response,
         FilterChain chain) throws ServletException, IOException
    {
        String authHeader = req.getHeader("Authorization"); 

        if (authHeader == null)
        {
            chain.doFilter(req, response); //next state/request
            return; 
        }

        if (authHeader.startsWith("Bearer "))
        {
            String token_raw = authHeader.substring(7); 
            String username = jwtUtil.extractUsername(token_raw); 
            
            if (username != null)
            {
                //creata a username password authentication token
                SecurityContext context = SecurityContextHolder.createEmptyContext(); 
                UserDetails CurrUser = Uservice.loadUserByUsername(username); 
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    CurrUser, 
                    null, 
                    CurrUser.getAuthorities()
                ); 
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        }    
        logger.info("Successfully authenticated user" + authHeader); 
        chain.doFilter(req, response);
    }
}
