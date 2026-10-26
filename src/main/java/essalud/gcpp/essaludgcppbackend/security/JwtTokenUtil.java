package essalud.gcpp.essaludgcppbackend.security;

import java.io.Serial;
import java.io.Serializable;

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
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;



    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String API_TOKEN_TYPE = "api";


    @Value(value = "${app.security.jwt.access-token-validity-ms}")
    private long accessTokenValidity;

    @Value(value = "${app.security.jwt.api-token-validity-ms}")
    private long apiTokenValidity;

    @Value(value = "${app.security.jwt.access-secret:}")
    private String accessSecretBase64;

    @Value(value = "${app.security.jwt.api-secret:}")
    private String apiSecretBase64;

    @Value(value = "${app.security.jwt.issuer}")
    private String issuer;


    // recuperar el nombre de usuario del token jwt
    @SuppressWarnings("null")
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // recuperar la fecha de caducidad del token jwt
    @SuppressWarnings("null")
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

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
                return parseClaimsWithKey(token, resolveSigningKey(API_TOKEN_TYPE));
            } catch (JwtException | IllegalArgumentException refreshEx) {
                throw new JwtException("Token invalido o firma no verificada", refreshEx);
            }
        }
    }

    // comprobar si el token ha caducado
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // generear token
    public String generateApiToken(UserDetails userDetails) {
        return generateToken(userDetails, apiTokenValidity, API_TOKEN_TYPE);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenValidity, ACCESS_TOKEN_TYPE);
    }

    @SuppressWarnings("null")
    private String generateToken(UserDetails userDetails, long validityMs, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_CLAIM,
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")));
        claims.put(TOKEN_TYPE_CLAIM, tokenType);
        claims.put(Claims.ID, UUID.randomUUID().toString());
        return doGenerateToken(claims, userDetails.getUsername(), validityMs);
    }

    public long getAccessTokenValiditySeconds() {
        return accessTokenValidity / 1000;
    }

    public long getRefreshTokenValiditySeconds() {
        return apiTokenValidity / 1000;
    }

    // compactación del JWT en una cadena segura para URL
    private String doGenerateToken(Map<String, Object> claims, String subject, long validityMs) {

        String tokenType = claims.get(TOKEN_TYPE_CLAIM).toString();
        SecretKey signingKey = resolveSigningKey(tokenType);

        // El algoritmo HMAC se elige segun el tamano de la clave (HS256 para 256 bits, HS512 para 512 bits).
        return Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + validityMs))
                .signWith(signingKey).compact();
    }

    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails, ACCESS_TOKEN_TYPE);
    }

    public Boolean validateApiToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails, API_TOKEN_TYPE);
    }

    // validar token
    private Boolean validateToken(String token, UserDetails userDetails, String expectedType) {
        final Claims claims;
        try {
            claims = parseClaimsWithKey(token, resolveSigningKey(expectedType));
        } catch (JwtException | IllegalArgumentException e) {
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

    private Claims parseClaimsWithKey(String token, SecretKey signingKey) {
        return Jwts.parser().verifyWith(signingKey).requireIssuer(issuer).build()
                .parseSignedClaims(token).getPayload();
    }

    // Access y api token usan claves distintas; se exige un minimo de 256 bits (Keys.hmacShaKeyFor).
    private SecretKey resolveSigningKey(String tokenType) {
        String configuredKey = ACCESS_TOKEN_TYPE.equals(tokenType) ? accessSecretBase64 : apiSecretBase64;

        if (configuredKey != null && !configuredKey.isBlank()) {
            return Keys.hmacShaKeyFor(Base64.getDecoder().decode(configuredKey));
        }

        throw new IllegalStateException("No se encontro una clave JWT configurada");
    }



}
