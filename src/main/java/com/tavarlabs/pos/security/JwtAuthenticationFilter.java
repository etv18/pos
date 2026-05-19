package com.tavarlabs.pos.security;

import com.tavarlabs.pos.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationService authenticationService;
    private static final String BEARER_ = "Bearer ";
    private final String ACCESS_TOKEN_COOKIE_KEYWORD = "accessToken";
    private final String REFRESH_TOKEN_COOKIE_KEYWORD = "refreshToken";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
    throws ServletException, IOException {
        try{
            String accessToken = extractToken(request, ACCESS_TOKEN_COOKIE_KEYWORD);
            String refreshToken = extractToken(request, REFRESH_TOKEN_COOKIE_KEYWORD);
            if(accessToken != null && refreshToken != null){
                UserDetails userDetails = authenticationService.validateToken(refreshToken, accessToken, response);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if(userDetails instanceof PosUserDetails){
                    request.setAttribute("userId", ((PosUserDetails) userDetails).getId());
                }
            }
        } catch (Exception e) {
            log.warn("Received invalid auth token");
            e.printStackTrace();
        }
        filterChain.doFilter(request, response); //Important to add this cause it'll need for the next chain of filters
    }

    private String extractToken(HttpServletRequest request, String tokenTypeCookieKeyword){
        //Get the token from the header first
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith(BEARER_))
            return bearerToken.substring(BEARER_.length());

        //If the code block above didnt find it try the cookies
        if(request.getCookies() != null)
            for(Cookie cookie : request.getCookies()) {
                if(tokenTypeCookieKeyword.equals(cookie.getName())) return cookie.getValue();
            }
        return null;
    }
}
