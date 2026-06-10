package com.springbootsampleray.store.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;;

/*
this file tells spring security how to handle http security requests
the auth...permitAll() means that only the auth layer can be access without security tokens

also allows JwtUtil.java to inject the password encoder bean
*/
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .anyRequest().authenticated()
        ).csrf(crsf -> crsf.disable()).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build(); 
    }
}
