package com.springbootsampleray.store.user;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.springbootsampleray.store.auth.dto.SignupRequest;
import com.springbootsampleray.store.auth.dto.LoginRequest;

@Service
public class UserService {
    
}

public String signup(SignupRequest req)
{
    return "sign up placeholder"; 
}

public String login(String username, String password) //going to reference login request objects here
{
    return "Log in placeholder"; 
}
