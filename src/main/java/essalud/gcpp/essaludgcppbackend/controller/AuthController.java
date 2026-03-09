package essalud.gcpp.essaludgcppbackend.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import essalud.gcpp.essaludgcppbackend.dtos.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.security.JwtTokenUtil;
import essalud.gcpp.essaludgcppbackend.serviceimplements.JwtUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
///@RequestMapping("/ggi")
public class AuthController {

    @Value("${app.security.cookie.access.name:access_token}")
    private String accessCookieName;

    @Value("${app.security.cookie.api.name:api_token}")
    private String apiCookieName;

    @Value("${app.security.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${app.security.cookie.same-site:Lax}")
    private String sameSite;

    @Value("${app.security.cookie.api.path:/}")
    private String apiCookiePath;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticateUser(@RequestBody UserLoginDto user) throws Exception {
        authenticateUser(user.getUsername(), user.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername());
        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        ResponseCookie accessCookie = ResponseCookie.from(accessCookieName, accessToken)
            .httpOnly(true)
            .secure(secureCookie)
            .path("/")
            .sameSite(sameSite)
            .maxAge(jwtTokenUtil.getAccessTokenValiditySeconds())
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from(apiCookieName, refreshToken)
            .httpOnly(true)
            .secure(secureCookie)
            .path(apiCookiePath)
            .sameSite(sameSite)
            .maxAge(jwtTokenUtil.getRefreshTokenValiditySeconds())
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body("Login Successfull");
    }

    @PostMapping("/access-token")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<?> validateAccessToken(HttpServletRequest request) {
        String accessToken = getCookieValue(request, accessCookieName);
        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(401).body("Access token requerido");
        }

        final String username;
        try {
            username = jwtTokenUtil.getUsernameFromToken(accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Access token invalido");
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (!jwtTokenUtil.validateAccessToken(accessToken, userDetails)) {
            return ResponseEntity.status(401).body("Access token invalido");
        }

        return ResponseEntity.ok("Access token valido");
    }
   
    @PostMapping("/refresh-token")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = getCookieValue(request, apiCookieName);
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).body("Refresh token requerido");
        }

        final String username;
        try {
            username = jwtTokenUtil.getUsernameFromToken(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Refresh token invalido");
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (!jwtTokenUtil.validateRefreshToken(refreshToken, userDetails)) {
            return ResponseEntity.status(401).body("Refresh token invalido");
        }

        String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails);
        ResponseCookie accessCookie = ResponseCookie.from(accessCookieName, newAccessToken)
            .httpOnly(true)
            .secure(secureCookie)
            .path("/")
            .sameSite(sameSite)
            .maxAge(jwtTokenUtil.getAccessTokenValiditySeconds())
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
            .body("Access token renovado");
    }

    @PostMapping("/logout") 
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie deleteAccessCookie = ResponseCookie.from(accessCookieName, "")
            .httpOnly(true)
            .secure(secureCookie)
            .path("/")
            .sameSite(sameSite)
            .maxAge(0)
            .build();

        ResponseCookie deleteApiCookie = ResponseCookie.from(apiCookieName, "")
            .httpOnly(true)
            .secure(secureCookie)
            .path(apiCookiePath)
            .sameSite(sameSite)
            .maxAge(0)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString())
            .header(HttpHeaders.SET_COOKIE, deleteApiCookie.toString())
            .body("Logout Successfull");
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private void authenticateUser(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch(DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }catch(BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }
}
