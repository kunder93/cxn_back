package es.org.cxn.backapp.filter;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT Authentication Filter that processes incoming requests and establishes
 * security context for valid JWT tokens. This filter:
 * <ul>
 * <li>Intercepts requests with Authorization headers</li>
 * <li>Validates JWT tokens using {@link DefaultJwtUtils}</li>
 * <li>Loads user details for valid tokens</li>
 * <li>Sets Spring Security authentication context</li>
 * </ul>
 *
 * <p>
 * <strong>Flow:</strong>
 * </p>
 * <ol>
 * <li>Check for Bearer token in Authorization header</li>
 * <li>Validate token signature and structure</li>
 * <li>Extract username from valid token</li>
 * <li>Load UserDetails from service</li>
 * <li>Validate token against user-specific details</li>
 * <li>Set authentication in security context</li>
 * </ol>
 *
 * @see OncePerRequestFilter Spring's base filter class
 * @see DefaultJwtUtils JWT validation utilities
 */
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * Length of bearer prefix 'Bearer '.
     */
    private static final int BEARER_PREFIX_LENGTH = 7;

    /**
     * Utility class for working with JWT tokens. Provides methods for token
     * validation and user extraction.
     */
    private final DefaultJwtUtils jwtUtils;

    /**
     * Service to load user details based on the username from the authentication
     * request.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructs a JWT authentication filter with required dependencies.
     *
     * @param jwtUtils           JWT utilities for token validation and processing
     * @param userDetailsService User details service for loading security
     *                           principals
     */
    public JwtRequestFilter(final DefaultJwtUtils jwtUtils, final UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Core filter method that processes JWT authentication.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param chain    Filter chain
     * @throws ServletException if request processing fails
     * @throws IOException      if I/O error occurs
     *
     *                          <p>
     *                          The filter:
     *                          </p>
     *                          <ul>
     *                          <li>Extracts Bearer token from Authorization
     *                          header</li>
     *                          <li>Validates token using
     *                          {@link DefaultJwtUtils#isTokenValid(String)}</li>
     *                          <li>Loads user details if token is valid</li>
     *                          <li>Sets authentication context using
     *                          {@link SecurityContextHolder}</li>
     *                          <li>Continues filter chain regardless of
     *                          authentication success</li>
     *                          </ul>
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain) throws ServletException, IOException {

        try {
            final String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(BEARER_PREFIX_LENGTH);

                if (jwtUtils.isTokenValid(jwt)) {
                    String username = jwtUtils.extractUsername(jwt);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                        if (jwtUtils.validateToken(jwt, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        chain.doFilter(request, response);
    }

    /**
     * Checks if the request method is unprotected for the specified URI.
     *
     * @param requestURI the request URI.
     * @param httpMethod the HTTP method.
     * @return {@code true} if the method is unprotected; {@code false} otherwise.
     */
    private boolean isUnprotectedMethod(final String requestURI, final String httpMethod) {
        return (AppURL.CHESS_QUESTION_URL.equals(requestURI) && "POST".equals(httpMethod))
                || (requestURI.matches("/api/[^/]+/lichessAuth") && "GET".equals(httpMethod))
                || (AppURL.PARTICIPANTS_URL.equals(requestURI) && "POST".equals(httpMethod));
    }

    /**
     * Checks if the request URI is unprotected.
     *
     * @param requestURI the request URI.
     * @return {@code true} if the URI is unprotected; {@code false} otherwise.
     */
    private boolean isUnprotectedUri(final String requestURI) {
        return requestURI.startsWith("/h2-console") || requestURI.startsWith(AppURL.SIGN_UP_URL)
                || requestURI.startsWith(AppURL.SIGN_IN_URL) || requestURI.startsWith("/api/address/getCountries")
                || requestURI.startsWith("/getAllLichessProfiles");
    }

    /**
     * Determines whether the request should bypass the filter.
     * <p>
     * Requests are skipped if they match specific unprotected URIs or methods.
     * </p>
     *
     * @param request the HTTP request.
     * @return {@code true} if the filter should be bypassed; {@code false}
     *         otherwise.
     */
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String httpMethod = request.getMethod();

        final boolean shouldSkip = isUnprotectedUri(requestURI) || isUnprotectedMethod(requestURI, httpMethod);
        logger.debug("Skipping JWT filter: ");
        return shouldSkip;
    }

}
