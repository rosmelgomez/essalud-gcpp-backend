package essalud.gcpp.essaludgcppbackend.security;


import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Value(value = "${app.security.cookie.access.name}")
    private String accessCookieName;

    @Value(value = "${app.security.cookie.api.name}")
    private String apiCookieName;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final boolean useApiToken = requiresApiToken(request);
        String username = null;
        String jwtToken = resolveToken(request, useApiToken);

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
             ////verificando
            boolean validToken = useApiToken
                ? jwtTokenUtil.validateApiToken(jwtToken, userDetails)
                : jwtTokenUtil.validateAccessToken(jwtToken, userDetails);

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

    private @Nullable String resolveToken(HttpServletRequest request, boolean useApiToken) {
        String tokenFromCookie = getTokenFromCookie(request, useApiToken ? apiCookieName : accessCookieName);
        if (tokenFromCookie != null && !tokenFromCookie.isBlank()) {
            return tokenFromCookie;
        }
        return null;
    }

    private  @Nullable String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }

        }
        return null;

    }

    private boolean requiresApiToken(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return requestPath.endsWith("/api-token")
            || requestPath.endsWith("/refresh-token")
            || requestPath.endsWith("/logout")
            || requestPath.endsWith("/logout-user");
    }
}
