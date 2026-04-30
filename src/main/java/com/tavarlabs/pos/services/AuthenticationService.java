package com.tavarlabs.pos.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AuthenticationService {
    UserDetails authenticate(String username, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);
    String getUrlBasedOnRole(List<GrantedAuthority> roles);
    void logoutUser(HttpServletResponse response);
}
