package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.enums.RoleName;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import com.tavarlabs.pos.services.AuthenticationService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final Long jwtExpirationTimeMs = 86400000L;

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public UserDetails authenticate(String username, String password) {
        //This manager handle the authentication to default provider
        //for creating this token, TODO: look deeper information about it
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return (UserDetails) authentication.getPrincipal();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(grantedAuthority -> {
                    return grantedAuthority.getAuthority();
                })
                .toList()
        );
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTimeMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UserDetails validateToken(String token) {
        /*
        * TODO: Create a org.springframework.security.core.userdetails.User obj
        *  instead of loading it from the database AND create a private method to extract the roles.
        * */
        String username = extractUserName(token);
        return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public String getUrlBasedOnRole(List<GrantedAuthority> roles) {
        List<String> rolesStr = roles.stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());
        String firstRole = rolesStr.getFirst();

        if(rolesStr.size() == 1 && firstRole.equals(RoleName.ROLE_CASHIER.name())) {
            return "/invoice/create";
        }
        else if (
                rolesStr.contains(RoleName.ROLE_ADMIN.name()) || rolesStr.contains(RoleName.ROLE_STAFF.name())
        ){
            return "/dashboard";
        } else {
            throw new BadCredentialsException("User roles are not valid...");
        }

    }

    @Override
    public void logoutUser(HttpServletResponse response) {
        /*
        * This way I delete the cookie which has the jwt which the browser has stored,
        * so it wouldn't be sent by him automatically anymore in every request as I
        * set it up in the login method at AuthController class.
        * */
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractUserName(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
