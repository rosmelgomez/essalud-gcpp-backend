package essalud.gcpp.essaludgcppbackend.security;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    public static final long DEFAULT_ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000;
    public static final long DEFAULT_REFRESH_TOKEN_VALIDITY = 7L * 24 * 60 * 60 * 1000;
    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final String DEFAULT_ISSUER = "essalud-gcpp-backend";

    @Value("${app.security.jwt.access-token-validity-ms:" + DEFAULT_ACCESS_TOKEN_VALIDITY + "}")
    private long accessTokenValidity;

    @Value("${app.security.jwt.refresh-token-validity-ms:" + DEFAULT_REFRESH_TOKEN_VALIDITY + "}")
    private long refreshTokenValidity;

    @Value("${app.security.jwt.access-secret-base64:}")
    private String accessSecretBase64;

    @Value("${app.security.jwt.refresh-secret-base64:}")
    private String refreshSecretBase64;

    // Legacy fallback for compatibility in environments that still use jwt.secret.
    @Value("${jwt.secret:}")
    private String legacySecret;

    @Value("${app.security.jwt.issuer:" + DEFAULT_ISSUER + "}")
    private String issuer;

    //recuperar el nombre de usuario del token jwt
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //recuperar la fecha de caducidad del token jwt
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);}

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Try access key first, then refresh key to support both token types.
    private Claims getAllClaimsFromToken(String token) {
        try {
            return parseClaimsWithKey(token, resolveSigningKey(ACCESS_TOKEN_TYPE));
        } catch (JwtException | IllegalArgumentException accessEx) {
            try {
                return parseClaimsWithKey(token, resolveSigningKey(REFRESH_TOKEN_TYPE));
            } catch (JwtException | IllegalArgumentException refreshEx) {
                throw new JwtException("Token invalido o firma no verificada", refreshEx);
            }
        }
    }

    //comprobar si el token ha caducado
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //generear token
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenValidity, ACCESS_TOKEN_TYPE);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenValidity, REFRESH_TOKEN_TYPE);
    }

    private String generateToken(UserDetails userDetails, long validityMs, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_CLAIM,
            userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        claims.put(TOKEN_TYPE_CLAIM, tokenType);
        claims.put(Claims.ID, UUID.randomUUID().toString());
        return doGenerateToken(claims, userDetails.getUsername(), validityMs);
    }

    public long getAccessTokenValiditySeconds() {
        return accessTokenValidity / 1000;
    }

    public long getRefreshTokenValiditySeconds() {
        return refreshTokenValidity / 1000;
    }

    // compactación del JWT en una cadena segura para URL
    private String doGenerateToken(Map<String, Object> claims, String subject, long validityMs) {

        String tokenType = claims.get(TOKEN_TYPE_CLAIM).toString();
        byte[] signingKey = resolveSigningKey(tokenType);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
            .setIssuer(issuer)
                .setExpiration(new Date(System.currentTimeMillis() + validityMs))
            .signWith(SignatureAlgorithm.HS512, signingKey).compact();
    }
    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails, ACCESS_TOKEN_TYPE);
    }

    public Boolean validateRefreshToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails, REFRESH_TOKEN_TYPE);
    }

    //validar token
    private Boolean validateToken(String token, UserDetails userDetails, String expectedType) {
        final Claims claims;
        try {
            claims = parseClaimsWithKey(token, resolveSigningKey(expectedType));
        } catch (JwtException e) {
            return false;
        }

        final String username = claims.getSubject();
        final String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        final String tokenIssuer = claims.getIssuer();

        return username != null
            && username.equals(userDetails.getUsername())
            && !isTokenExpired(token)
            && expectedType.equals(tokenType)
            && issuer.equals(tokenIssuer);
    }

    private Claims parseClaimsWithKey(String token, byte[] signingKey) {
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
    }

    private byte[] resolveSigningKey(String tokenType) {
        String configuredKey = ACCESS_TOKEN_TYPE.equals(tokenType) ? accessSecretBase64 : refreshSecretBase64;

        if (configuredKey != null && !configuredKey.isBlank()) {
            return Base64.getDecoder().decode(configuredKey);
        }

        if (legacySecret != null && !legacySecret.isBlank()) {
            return legacySecret.getBytes(StandardCharsets.UTF_8);
        }

        throw new IllegalStateException("No se encontro una clave JWT configurada");
    }
}
