package com.springbootsampleray.store.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springbootsampleray.store.user.User;
import com.springbootsampleray.store.user.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.String;

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
    private UserService Uservice; 

    @Override
    protected void doFilterInternal(HttpServletRequest req, 
        HttpServletResponse response,
         FilterChain chain) throws ServletException, IOException
    {
        String authHeader = req.getHeader("Authorization"); 

        try
        {
            if (authHeader.startsWith("Bearer "))
            {
                String token_raw = authHeader.substring(6); 
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

                logger.info("Successfully authenticated user" + authHeader); 
                chain.doFilter(req, response);
            }    
        }
        catch (NullPointerException e)
        {
            System.out.println("Error: authorization header is null " + e.getMessage()); 
        }
    }
}
