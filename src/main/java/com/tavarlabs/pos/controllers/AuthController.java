package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.auth.AuthResponse;
import com.tavarlabs.pos.dtos.auth.LoginRequest;
import com.tavarlabs.pos.entity.User;
import com.tavarlabs.pos.repositories.UserRepository;
import com.tavarlabs.pos.security.PosUserDetailsService;
import com.tavarlabs.pos.services.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
    private final UserRepository repo;

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        String token = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .expiresIn(86400)
                .build();
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/test")
    public String testingAuth(HttpServletRequest request){
        Long userId = (long) request.getAttribute("userId");
        User user = repo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Entity with not found. id = " + userId)
        );
        return "Welcome: " + user.getFullName();
    }

    @GetMapping("/autoreload")
    public String testingRelaod(){
        return "Hello Emmanuel 2";
    }
}
