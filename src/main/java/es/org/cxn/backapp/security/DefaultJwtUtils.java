
package es.org.cxn.backapp.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Class for operations associated to jwt token used in authentication.
 *
 * @author Santi.
 *
 */
@Service
public final class DefaultJwtUtils {
    /**
     * Temporary for develop, must change.
     */
    private static final String SECRET = "mysecretpasswordjwttyrtyrtgdfyryrytjhjgrtyrtyrmhgjfrtyrty";

    /**
     * The duration for which the JWT token is valid.
     * <p>
     * This constant defines the expiration time of a JWT token. The token will be
     * valid for a period of 10 hours from the time it is issued. This duration is
     * represented using {@link java.time.Duration}.
     * <p>
     * Example usage:
     *
     * <pre>
     * Instant now = Instant.now();
     * Instant expiration = now.plus(EXPIRATION_TIME);
     * </pre>
     *
     * The token will expire when the current time surpasses the {@code expiration}
     * instant.
     */
    private static final Duration EXPIRATION_TIME = Duration.ofHours(10);

    /**
     * Private no args constructor.
     */
    private DefaultJwtUtils() {
        // private unnecessary constructor for utility class.
    }

    /**
     * Creates a JWT token with the specified claims and subject.
     *
     * @param claims  A map of claims to be included in the token. Claims are
     *                key-value pairs that represent various pieces of information
     *                about the subject.
     * @param subject The subject of the token, usually representing the user or
     *                entity. the token is associated with.
     * @return A JWT token as a {@link String} that contains the provided claims and
     *         subject, and is signed with the specified signing key.
     */
    private static String createToken(final Map<String, Object> claims, final String subject) {
        final var now = Instant.now();
        final var expiration = now.plus(EXPIRATION_TIME);

        return Jwts.builder().claims(claims).subject(subject).issuedAt(Date.from(now)).expiration(Date.from(expiration))
                .signWith(getSigningKey()).compact();
    }

    /**
     * Get all claims from the JWT token.
     *
     * @param token the JWT token.
     * @return the Claims {@link Claims}.
     * @throws JwtException if the token is invalid or parsing fails.
     */
    public static Claims extractAllClaims(final String token) {

        final var signKey = getSigningKey();
        // Create a JWT parser with the signing key
        final var jwtParser = Jwts.parser().verifyWith(signKey).build();
        // Parse the token and extract claims
        final var jws = jwtParser.parseSignedClaims(token);
        return jws.getPayload();
    }

    /**
     * Extract claim from jwt token.
     *
     * @param <T>            value of claim which want extract.
     * @param token          the jwt token.
     * @param claimsResolver function for filter wanted claim.
     * @return the claim filtered from jwt token.
     */
    public static <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract expiration date from jwt token.
     *
     * @param token the jwt token.
     * @return expiration date.
     */
    public static Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract username from jwt token.
     *
     * @param token the jwt token.
     * @return the username.
     */
    public static String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generate jwt token.
     *
     * @param userDetails the userDetails used for generate, only used UserName(user
     *                    email).
     * @return the jwt token.
     */
    public static String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new ConcurrentHashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Temporary for develop, must change.
     *
     * @return the key.
     */
    private static SecretKey getSigningKey() {
        final var keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Check if a token has expired.
     *
     * @param token the JWT token.
     * @return true if the token has expired, false otherwise.
     */
    public static Boolean isTokenExpired(final String token) {
        final var expirationDate = extractExpiration(token);
        final var expirationInstant = expirationDate.toInstant();
        final var now = Instant.now();
        return expirationInstant.isBefore(now);
    }

    /**
     * Validate if token is generated from user.
     *
     * @param token       the jwt token.
     * @param userDetails the user.
     * @return true if is valid false if not.
     */
    public static Boolean validateToken(final String token, final UserDetails userDetails) {
        final var userName = extractUsername(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
