package com.springbootsampleray.store.auth.dto;

public class AuthResponse {
    private String message; 
    private String token; 
}

public AuthResponse(String msg, String tkn)
{
    this.message = msg;
    this.token = tkn; 
}
