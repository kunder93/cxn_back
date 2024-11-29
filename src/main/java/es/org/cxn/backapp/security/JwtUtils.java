/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.security;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

/**
 * Service interface for JWT token operations.
 * Provides methods for extracting information from tokens, generating tokens,
 * and validating tokens.
 *
 * @author Santiago Paz
 */
public interface JwtUtils {

  /**
   * Extracts the username from the JWT token.
   *
   * @param token the JWT token.
   * @return the extracted username.
   */
  String extractUsername(String token);

  /**
   * Extracts the expiration date from the JWT token.
   *
   * @param token the JWT token.
   * @return the expiration date of the JWT token.
   */
  Date extractExpiration(String token);

  /**
   * Extracts a specific claim from the JWT token based on the provided
   * function.
   *
   * @param <T>            the type of the claim to be extracted.
   * @param token          the JWT token.
   * @param claimsResolver function to extract the desired claim.
   * @return the extracted claim.
   */
  <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

  /**
   * Generates a JWT token based on user details.
   *
   * @param userDetails the user details used to generate the token.
   * @return the generated JWT token.
   */
  String generateToken(MyPrincipalUser userDetails);

  /**
   * Validates the JWT token by comparing it with the user details.
   *
   * @param token       the JWT token.
   * @param userDetails the user details to validate against.
   * @return true if the token is valid, false otherwise.
   */
  Boolean validateToken(String token, UserDetails userDetails);
}
