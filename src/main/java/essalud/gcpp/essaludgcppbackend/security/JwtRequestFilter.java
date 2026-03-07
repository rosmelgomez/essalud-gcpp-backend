package essalud.gcpp.essaludgcppbackend.security;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import essalud.gcpp.essaludgcppbackend.serviceimplements.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Value("${app.security.cookie.access.name:access_token}")
    private String accessCookieName;

    @Value("${app.security.cookie.refresh.name:api_token}")
    private String refreshCookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final boolean useAccessToken = requiresAccessToken(request);
        String username = null;
        String jwtToken = resolveToken(request, authHeader, useAccessToken);

        if (jwtToken != null) {
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("no se puede encontrar el token");
            } catch (ExpiredJwtException e) {
                System.out.println("el token es expired");
            } catch (Exception e) {
                logger.warn("JWT invalido");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            boolean validToken = useAccessToken
                ? jwtTokenUtil.validateAccessToken(jwtToken, userDetails)
                : jwtTokenUtil.validateRefreshToken(jwtToken, userDetails);

            if (validToken) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request, String authHeader, boolean useAccessToken) {
        String tokenFromCookie = getTokenFromCookie(request, useAccessToken ? accessCookieName : refreshCookieName);
        if (tokenFromCookie != null && !tokenFromCookie.isBlank()) {
            return tokenFromCookie;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.split(" ")[1].trim();
        }

        return null;
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
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

    private boolean requiresAccessToken(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return !"/ggi/refresh-token".equals(requestPath);
    }
}
