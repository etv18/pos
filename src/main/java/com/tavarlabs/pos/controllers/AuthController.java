package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.auth.AuthResponse;
import com.tavarlabs.pos.dtos.auth.LoginRequest;
import com.tavarlabs.pos.entity.User;
import com.tavarlabs.pos.repositories.UserRepository;
import com.tavarlabs.pos.security.PosUserDetailsService;
import com.tavarlabs.pos.services.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
    private final UserRepository repo;

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ){
        UserDetails userDetails = authenticationService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        String jwt = authenticationService.generateToken(userDetails);
        String url = authenticationService.getUrlBasedOnRole(new ArrayList<>(userDetails.getAuthorities()));

        AuthResponse authResponse = AuthResponse.builder()
                .token(jwt)
                .expiresIn(86400)
                .url(url)
                .build();

        /*
        * I set up this way to send jwt due to I'm using a rest architecture in the backend,
        * and I'm using thymeleaf for the views it's easier to set up the jwt in Http Only Cookie
        * so that way the browser stores it and send automatically to the backend in every request.
        *
        * This way I'm able to use thymeleaf and take advantages of its functionalities with no
        * headaches about manually using js to send the token.
         * */
        ResponseCookie cookie = ResponseCookie.from("token", jwt)
                .httpOnly(true)
                .path("/")
                .sameSite("Lax") // This means: send the cookie in normal navigation, like 'clicks' on links, etc...
                .maxAge(Duration.ofDays(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

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
