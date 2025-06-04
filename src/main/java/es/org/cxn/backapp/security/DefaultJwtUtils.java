
package es.org.cxn.backapp.security;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import es.org.cxn.backapp.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service class for handling JWT operations including token generation,
 * validation, and claim extraction. This class implements core JWT
 * functionality following the JJWT library specifications.
 *
 * <p>
 * Responsible for:
 * </p>
 * <ul>
 * <li>Token generation with configurable expiration</li>
 * <li>Claim extraction from JWT tokens</li>
 * <li>Token validation and expiration checks</li>
 * <li>User-specific token validation</li>
 * </ul>
 *
 * @see JwtProperties Configuration properties for JWT setup
 * @see UserDetails Spring Security user details interface
 */
@Service
public class DefaultJwtUtils {

    /**
     * Secret key used to sign and verify JWT tokens.
     *
     * <p>
     * The signing key should be kept confidential and must be securely stored. It
     * is used to ensure that the JWT is valid and was issued by a trusted source.
     */
    private final SecretKey signingKey;

    /**
     * Duration for which the JWT token is valid.
     *
     * <p>
     * This duration specifies the expiration time for the generated JWT token.
     * Tokens that expire before use should be considered invalid, requiring
     * re-authentication or token renewal.
     */
    private final Duration expirationTime;

    /**
     * Constructs a JWT utilities instance with configuration properties.
     *
     * @param jwtProperties Configuration properties containing:
     *                      <ul>
     *                      <li>secret: Base64-encoded secret key</li>
     *                      <li>expiration: Token validity duration in seconds</li>
     *                      </ul>
     */
    public DefaultJwtUtils(final JwtProperties jwtProperties) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
        this.expirationTime = Duration.ofSeconds(jwtProperties.getExpiration());
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token JWT token to parse
     * @return Claims object containing all token claims
     * @throws JwtException if the token is invalid or cannot be parsed
     */
    public Claims extractAllClaims(final String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Extracts a specific claim from the token using a claims resolver function.
     *
     * @param <T>            Type of the claim to extract
     * @param token          JWT token to process
     * @param claimsResolver Function to extract specific claim from Claims object
     * @return The resolved claim value
     */
    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the expiration time from the token as an Instant.
     *
     * @param token JWT token to inspect
     * @return Instant representing token expiration time
     */
    public Instant extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    /**
     * Extracts the username (subject) from the token.
     *
     * @param token JWT token to inspect
     * @return Username stored in the token subject
     */
    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a new JWT token for a user.
     *
     * @param userDetails User details to include in the token
     * @return Signed JWT token containing:
     *         <ul>
     *         <li>Subject: Username</li>
     *         <li>IssuedAt: Current time</li>
     *         <li>Expiration: Current time + configured duration</li>
     *         </ul>
     */
    public String generateToken(final UserDetails userDetails) {
        return Jwts.builder().subject(userDetails.getUsername()).issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(expirationTime))).signWith(signingKey).compact();
    }

    /**
     * Checks if a token has expired.
     *
     * @param token JWT token to validate
     * @return true if token is expired, false otherwise
     * @throws JwtException if the token cannot be parsed
     */
    public boolean isTokenExpired(final String token) {
        boolean isExpired = false;

        try {
            final Instant expiration = extractExpiration(token);
            isExpired = expiration.isBefore(Instant.now());
        } catch (ExpiredJwtException ex) {
            isExpired = true;
        }
        return isExpired;
    }

    /**
     * Validates basic token integrity (signature and format).
     *
     * @param token JWT token to validate
     * @return true if token is properly signed and formatted, false otherwise
     */
    public boolean isTokenValid(final String token) {
        boolean isValid;

        try {
            Jwts.parser().verifyWith(signingKey).build().parse(token);
            isValid = true;
        } catch (JwtException | IllegalArgumentException e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Full validation of token for a specific user.
     *
     * @param token       JWT token to validate
     * @param userDetails User details to validate against
     * @return true if all conditions are met:
     *         <ul>
     *         <li>Token is properly signed</li>
     *         <li>Token is not expired</li>
     *         <li>Username matches token subject</li>
     *         </ul>
     */
    public boolean validateToken(final String token, final UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (ExpiredJwtException ex) {
            return false;
        }
    }
}
