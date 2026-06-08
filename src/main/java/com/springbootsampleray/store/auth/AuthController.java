package com.springbootsampleray.store.auth;
import com.springbootsampleray.store.user.UserService;

//RestController, RequestMapping, PostMapping, RequestBody
import org.springframework.web.bind.annotation.*;
//register request, login request, and auth response
import com.springbootsampleray.store.auth.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    public UserService userService; 

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest userReq)
    {
        try{
            log.info("Signing up new user: {}", userReq.getUsername()); 
            String token = userService.signup(userReq); 
            return ResponseEntity.ok(new AuthResponse("User signed up successfully. ", token)); 
        }
        catch(Exception e){
            log.error("Error during signup: {}", e.getMessage()); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new AuthResponse("Signed up failed", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest userReq){
        try{
            log.info("Logging in user {}", userReq.getUsername()); 
            String token = userService.login(userReq.getUsername(), userReq.getPassword()); 
            return ResponseEntity.ok(new AuthResponse("Login successful.", token)); 
        }
        catch(Exception e){
            log.error("Error during login: {}", e.getMessage()); 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new AuthResponse("Login failed", e.getMessage())); 
        }
    }
}
