package essalud.gcpp.essaludgcppbackend.controller.usuario;

import org.jspecify.annotations.Nullable;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.security.JwtTokenUtil;
import essalud.gcpp.essaludgcppbackend.serviceimplements.JwtUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Value("${app.security.cookie.access.name}")
    private String accessCookieName;

    @Value("${app.security.cookie.api.name}")
    private String apiCookieName;

    @Value("${app.security.cookie.secure}")
    private boolean secureCookie;

    @Value("${app.security.cookie.same-site}")
    private String sameSite;

    @Value("${app.security.cookie.api.path}")
    private String apiCookiePath;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
            JwtUserDetailsService jwtUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticateUser(@RequestBody UserLoginDto user) throws Exception {
        authenticateUser(user.getUsername(), user.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername());
        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        final String apiToken = jwtTokenUtil.generateApiToken(userDetails);

        ResponseCookie accessCookie = ResponseCookie.from(accessCookieName, accessToken)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .sameSite(sameSite)
                .maxAge(jwtTokenUtil.getAccessTokenValiditySeconds())
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(apiCookieName, apiToken)
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

    @GetMapping("/api-token")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<?> validateApiToken(HttpServletRequest request) {
        String apiToken = getCookieValue(request, apiCookieName);
        if (apiToken == null || apiToken.isBlank()) {
            return ResponseEntity.status(401).body("Api token requerido");
        }

        final String username;
        try {
            username = jwtTokenUtil.getUsernameFromToken(apiToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Api token invalido");
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (!jwtTokenUtil.validateApiToken(apiToken, userDetails)) {
            return ResponseEntity.status(401).body("Api token invalido");
        }

        return ResponseEntity.ok("Api token valido");
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
        if (!jwtTokenUtil.validateApiToken(refreshToken, userDetails)) {
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

    @PostMapping({ "/logout", "/logout-user" })
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

    private @Nullable String getCookieValue(HttpServletRequest request, String cookieName) {
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
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }
}
