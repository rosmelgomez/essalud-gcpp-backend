package essalud.gcpp.essaludgcppbackend.controller.usuario;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsuarioLoginDto;
import essalud.gcpp.essaludgcppbackend.security.JwtTokenUtil;
import essalud.gcpp.essaludgcppbackend.security.LoginAttemptService;
import essalud.gcpp.essaludgcppbackend.serviceQA.JwtUserDetailsServ;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthCon {

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
    private final JwtUserDetailsServ jwtUserDetailsServ;
    private final LoginAttemptService loginAttemptService;

    public AuthCon(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                   JwtUserDetailsServ jwtUserDetailsServ, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsServ = jwtUserDetailsServ;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticateUser(@RequestBody UsuarioLoginDto user) {
       if (user.getUsername() != null && user.getPassword() != null) {
           if (loginAttemptService.isBlocked(user.getUsername())) {
               return ResponseEntity.status(429).body("Demasiados intentos fallidos. Intente mas tarde.");
           }
           try {
               authenticationManager.authenticate(
                       new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
           } catch (AuthenticationException e) {
               loginAttemptService.loginFailed(user.getUsername());
               return ResponseEntity.status(401).body("Credenciales invalidas");
           }
           loginAttemptService.loginSucceeded(user.getUsername());
           final UserDetails userDetails = jwtUserDetailsServ.loadUserByUsername(user.getUsername());
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
       return ResponseEntity.badRequest().body("Usuario y contrasena requeridos");
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

        UserDetails userDetails = jwtUserDetailsServ.loadUserByUsername(username);
        if (!jwtTokenUtil.validateApiToken(apiToken, userDetails)) {
            return ResponseEntity.status(401).body("Api token invalido");
        }

        return ResponseEntity.ok("Api token valido");
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
}
