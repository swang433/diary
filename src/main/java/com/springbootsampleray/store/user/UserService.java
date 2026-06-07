package com.springbootsampleray.store.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.springbootsampleray.store.auth.dto.SignupRequest;
import com.springbootsampleray.store.security.JwtUtil;

@Service
public class UserService {
    @Autowired
    private UserRepo URepo;
    @Autowired 
    private JwtUtil Encrypt; 
    @Autowired
    private PasswordEncoder Encoder; 

    public String signup(SignupRequest req)
    {
        String newName = req.getUsername(); 
        if (URepo.findByUsername(newName) != null)
        {
            throw new RuntimeException("User already exists."); 
        }
        else
        {
            User NewUser = new User(); 
            NewUser.setUsername(newName);
            String hashed_password = Encoder.encode(req.getPassword()); 
            //store hashed password as param in the DB
            NewUser.setPassword(hashed_password); 
            URepo.save(NewUser); 
            return Encrypt.generateToken(newName);
        }
    }

    public String login(String username, String password) //going to reference login request objects here
    {
        if (URepo.findByUsername(username) == null)
        {   
            throw new RuntimeException("Cannot find username in the system."); 
        }
        else 
        {
            User CurrUser = URepo.findByUsername(username); 
            //check against hashed version of the password
            if (!Encoder.matches(password, CurrUser.getPassword())) //incorrect password path 
            {
                throw new RuntimeException("Failure to authenticate: incorrect password. "); 
            }
            else //correct password path
            {
                return Encrypt.generateToken(username); 
            }
        }
    }
}


