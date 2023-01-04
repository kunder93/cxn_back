/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.service;

import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

/**
 * Service for jwt token.
 *
 * @author Santiago Paz.
 */
public interface JwtUtils {

    /**
     * Extract userName from jwt token.
     *
     * @param token the jwt token.
     * @return extracted userName.
     */
    String extractUsername(String token);

    /**
     * Returns date when jwt token expires.
     *
     * @param token the jwt token.
     * @return the Date when jwt token expires.
     */
    Date extractExpiration(String token);

    /**
     * Returns claims that pass funcion from jwt token.
     *
     * @param <T>            claims type.
     * @param token          the jwt token.
     * @param claimsResolver function to extract claims.
     * @return the claims extracted.
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generate jwt token from user data.
     *
     * @param userDetails the user data.
     * @return jwt token.
     */
    String generateToken(MyPrincipalUser userDetails);

    /**
     * Validate a jwt token through user data.
     *
     * @param token       the jwt token.
     * @param userDetails the user data.
     * @return true if validated false if not.
     */
    Boolean validateToken(String token, UserDetails userDetails);

}
