package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.enums.RoleName;
import io.jsonwebtoken.*;

import com.tavarlabs.pos.services.AuthenticationService;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final String ACTIVE = "active";
    private final String ACCESS_TOKEN = "access";
    private final String REFRESH_TOKEN = "refresh";
    private final String ACCESS_TOKEN_COOKIE_KEYWORD = "accessToken";
    private final String REFRESH_TOKEN_COOKIE_KEYWORD = "refreshToken";
    private final Long ACCESS_JWT_EXP_IN_MS = 2000L;
    private final Long REFRESH_JWT_EXP_IN_MS = 3000L;

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
    public String generateToken(UserDetails userDetails, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(grantedAuthority -> {
                    return grantedAuthority.getAuthority();
                })
                .toList()
        );
        claims.put("type", tokenType);
        claims.put(ACTIVE, userDetails.isEnabled());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + setJwtExpirationTimeInMs(tokenType)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UserDetails validateToken(String refreshToken, String accessToken, HttpServletResponse response) {
        Claims claims = parseJwtToClaims(accessToken);

        Boolean enabled = claims.get(ACTIVE, Boolean.class);
        if(!enabled) {
            throw new DisabledException("Your account is disabled, please contact your IT admin.");
        }

        String username = extractUserName(accessToken);
        List<SimpleGrantedAuthority> authorities = extractAuthorities(accessToken);
        UserDetails user = new org.springframework.security.core.userdetails.User(username, "", authorities);

        if(isTokenExpired(accessToken)){
             user = userDetailsService.loadUserByUsername(username);

            if(isTokenExpired(refreshToken)){
                refreshToken = generateToken(user, REFRESH_TOKEN);
                setHttpOnlyCookie(refreshToken, REFRESH_TOKEN_COOKIE_KEYWORD, response);
            }

            accessToken = generateToken(user, ACCESS_TOKEN);
            setHttpOnlyCookie(accessToken, ACCESS_TOKEN_COOKIE_KEYWORD, response);
        }
        return user;
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
        ResponseCookie accessTokenCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_KEYWORD, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_KEYWORD, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    public boolean isTokenExpired(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Do sth over here later
        }
        return false;
    }

    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractUserName(String token){
        Claims claims = parseJwtToClaims(token);
        return claims.getSubject();
    }

    private List<SimpleGrantedAuthority> extractAuthorities(String token){
        Claims claims = parseJwtToClaims(token);
        List<String> roles = (List<String>) claims.get("roles");
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority((String) r))
                .toList();
    }

    private Claims parseJwtToClaims(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private Long setJwtExpirationTimeInMs(String tokenType){
        if(tokenType.equalsIgnoreCase(ACCESS_TOKEN)){
            return ACCESS_JWT_EXP_IN_MS;
        }
        return REFRESH_JWT_EXP_IN_MS;
    }

    private void setHttpOnlyCookie(String token, String tokenKeyword, HttpServletResponse response){
        ResponseCookie responseCookie = ResponseCookie.from(tokenKeyword, token)
                .httpOnly(true)
                .path("/")
                .sameSite("Lax") // This means: send the cookie in normal navigation, like 'clicks' on links, etc...
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
