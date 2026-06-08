package com.springbootsampleray.store.security;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter{
    
}
