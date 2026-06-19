package com.springbootsampleray.store.auth;

import com.springbootsampleray.store.user.UserService;

import org.springframework.web.bind.annotation.*;
import com.springbootsampleray.store.auth.dto.*;
import java.lang.System.Logger;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    public static final Logger logger = System.getLogger(AuthController.class.getName());

    public AuthController(UserService UService)
    {
        this.userService = UService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest userReq)
    {
        logger.log(System.Logger.Level.INFO, "Sign up attempt: " + userReq.getUsername());
        String token = userService.signup(userReq);
        return ResponseEntity.ok(new AuthResponse("User signed up successfully. ", token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest userReq)
    {
        logger.log(System.Logger.Level.INFO, "Login attempt: " + userReq.getUsername());
        String token = userService.login(userReq.getUsername(), userReq.getPassword());
        return ResponseEntity.ok(new AuthResponse("Login successful.", token));
    }
}
