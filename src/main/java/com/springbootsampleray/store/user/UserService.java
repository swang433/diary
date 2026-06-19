package com.springbootsampleray.store.user;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.springbootsampleray.store.auth.dto.SignupRequest;
import com.springbootsampleray.store.security.JwtUtil;
import com.springbootsampleray.store.exceptions.DuplicateUserException;
import com.springbootsampleray.store.exceptions.InvalidCredentialsException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.springbootsampleray.store.entry.*;

@Service
public class UserService implements UserDetailsService{
    private final UserRepo URepo;
    private final JwtUtil Encrypt;
    private final PasswordEncoder Encoder; 

    public UserService(UserRepo repo, JwtUtil util, PasswordEncoder encoder)
    {
        this.URepo = repo;
        this.Encrypt = util; 
        this.Encoder = encoder; 
    }

    public String signup(SignupRequest req)
    {
        String newName = req.getUsername(); 
        if (URepo.findByUsername(newName) != null)
        {
            throw new DuplicateUserException("User already exists.");
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
            throw new InvalidCredentialsException("Cannot find username in the system.");
        }
        else 
        {
            User CurrUser = URepo.findByUsername(username); 
            //check against hashed version of the password
            if (!Encoder.matches(password, CurrUser.getPassword())) //incorrect password path 
            {
                throw new InvalidCredentialsException("Failure to authenticate: incorrect password. ");
            }
            else //correct password path
            {
                return Encrypt.generateToken(username); 
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        if (URepo.findByUsername(username) == null)
        {   
            throw new UsernameNotFoundException("User not found found in the system."); 
        }
        else
        {
            User CurrUser = URepo.findByUsername(username); 
            return new org.springframework.security.core.userdetails.User(username, 
                CurrUser.getPassword(), 
                new ArrayList<>()); 
        }
    }

    public User FindUserInService(String username) 
    {
        User UserResult = URepo.findByUsername(username); 
        if (UserResult == null)
        {
            //if user not found able to catch it in the controller layer
            throw new UsernameNotFoundException("Username not found in the system. "); 
        }
        else 
        {
            return UserResult; 
        }
    }
}


