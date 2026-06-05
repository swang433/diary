package com.springbootsampleray.store.user;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.springbootsampleray.store.auth.dto.SignupRequest;

@Service
public class UserService {
    
}

public String signup(SignupRequest req)
{
    return "sign up placeholder"; 
}

public String login(String username, String password)
{
    return "Log in placeholder"; 
}
