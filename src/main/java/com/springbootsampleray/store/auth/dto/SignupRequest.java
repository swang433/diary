package com.springbootsampleray.store.auth.dto;
import lombok.Data;

@Data //generates a noArgs constructor
public class SignupRequest {
    private String username; 
    private String password; 
}
