package com.springbootsampleray.store.auth.dto;
import lombok.Data;

@Data //generates a noArgs constructor
public class SignupRequest {
    private String username; 
    private String password; 

    public String getUsername()
    {
        return this.username; 
    }

    public String getPassword()
    {
        return this.password; 
    }
}
