
package es.org.cxn.backapp.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
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
public final class DefaultJwtUtils implements JwtUtils {

  /**
   * Temporary for develop, must change.
   */
  private static final String SECRET =
        "mysecretpasswordjwttyrtyrtgdfyryrytjhjgrtyrtyrmhgjfrtyrty";

  /**
   * JWT Expiration time const.
   */
  private static final int EXPIRATION_TIME = 1000 * 60 * 60 * 10;

  /**
   * Default constructor.
   */
  public DefaultJwtUtils() {
    super();
  }

  /**
   * Temporary for develop, must change.
   *
   * @return the key.
   */
  private static Key getSigningKey() {
    var keyBytes = Decoders.BASE64.decode(DefaultJwtUtils.SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Extract username from jwt token.
   *
   * @param token the jwt token.
   * @return the username.
   */
  @Override
  public String extractUsername(final String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extract expiration date from jwt token.
   *
   * @param token the jwt token.
   * @return expiration date.
   */
  @Override
  public Date extractExpiration(final String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extract claim from jwt token.
   *
   * @param <T>            value of claim which want extract.
   * @param token          the jwt token.
   * @param claimsResolver function for filter wanted claim.
   * @return the claim filtered from jwt token.
   */
  @Override
  public <T> T extractClaim(
        final String token, final Function<Claims, T> claimsResolver
  ) {
    final var claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Get all claims from Jwt token.
   *
   * @param token the jwt token.
   * @return the Claims {@link Claims}.
   */
  private Claims extractAllClaims(final String token) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
          .parseClaimsJws(token).getBody();
  }

  /**
   * check if a token has expired.
   *
   * @param token the jwt token.
   * @return true if yes, false if not @see Boolean.
   */
  private Boolean isTokenExpired(final String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Generate jwt token.
   *
   * @param userDetails the userDetails used for generate, only used
   *                    UserName(user email).
   * @return the jwt token.
   */
  @Override
  public String generateToken(final MyPrincipalUser userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername());
  }

  private String
        createToken(final Map<String, Object> claims, final String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject)
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
          .signWith(getSigningKey()).compact();
  }

  /**
   * Validate if token is generated from user.
   *
   * @param token       the jwt token.
   * @param userDetails the user.
   * @return true if is valid false if not.
   */
  @Override
  public Boolean
        validateToken(final String token, final UserDetails userDetails) {
    final var userName = extractUsername(token);
    return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

}
