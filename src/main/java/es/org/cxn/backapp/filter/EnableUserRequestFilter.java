package es.org.cxn.backapp.filter;

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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.exceptions.DisabledUserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A custom filter that checks if the authenticated user is enabled before
 * processing the request. This filter extends {@link OncePerRequestFilter} to
 * ensure it is executed once per request.
 *
 * The filter performs the following: - Validates if the authenticated user is
 * enabled. - Clears the security context and throws an exception if the user is
 * disabled. - Skips filtering for specific URLs such as `/h2-console` or
 * authentication-related endpoints.
 *
 * Dependencies: - {@link UserDetailsService} to load user details and verify if
 * the user is enabled. - {@link SecurityContextHolder} to manage the security
 * context.
 *
 * Exclusions: - Certain endpoints are excluded from this filter via the
 * {@link #shouldNotFilter(HttpServletRequest)} method.
 */
@Component
public class EnableUserRequestFilter extends OncePerRequestFilter {

    /**
     * The Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EnableUserRequestFilter.class);

    /**
     * The {@link UserDetailsService} used to load user details.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructs an instance of {@code EnableUserRequestFilter}.
     *
     * @param usrDetailsService the {@link UserDetailsService} to use for user
     *                          validation.
     */
    public EnableUserRequestFilter(final UserDetailsService usrDetailsService) {
        super();
        this.userDetailsService = usrDetailsService;
    }

    /**
     * Filters requests to check if the authenticated user is enabled. If the user
     * is disabled, clears the security context and throws an
     * {@link IllegalStateException}.
     *
     * @param request     the HTTP request.
     * @param response    the HTTP response.
     * @param filterChain the filter chain to pass the request to the next filter.
     *
     * @throws ServletException if a servlet-related error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("EnableUserRequestFilter: Processing request.");
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            final String email = authentication.getName();
            LOGGER.info("Authentication detected for user: {}", email);

            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                if (!userDetails.isEnabled()) {
                    LOGGER.warn("User '{}' is disabled. Clearing security context.", email);
                    SecurityContextHolder.clearContext();
                    throw new DisabledUserException("User is disabled.");
                }
            } else {
                // Fallback to loading from UserDetailsService if not available
                final boolean isUserEnabled = userDetailsService.loadUserByUsername(email).isEnabled();
                if (!isUserEnabled) {
                    LOGGER.warn("User '{}' is disabled. Clearing security context.", email);
                    SecurityContextHolder.clearContext();
                    throw new DisabledUserException("User is disabled.");
                }
            }
            LOGGER.info("User '{}' is enabled. Proceeding with request.", email);
        } else {
            LOGGER.info("No valid authentication found. Proceeding without user validation.");
        }
        LOGGER.info("Request processing completed. Passing to the next filter.");
        filterChain.doFilter(request, response);
    }

    /**
     * Determines whether this filter should not apply to a given request. Excludes
     * requests to specific endpoints such as `/h2-console`, `/sign-up`, and others.
     *
     * @param request the current HTTP request.
     * @return {@code true} if the request should not be filtered; {@code false}
     *         otherwise.
     *
     * @throws ServletException if a servlet-related error occurs.
     */
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        final var requestURI = request.getRequestURI();
        final var httpMethod = request.getMethod();
        LOGGER.info("La URI en shouldNotFilter de ENABLE USER es: {}", requestURI);
        final var value = requestURI.startsWith("/h2-console") || requestURI.startsWith(AppURL.SIGN_UP_URL)
                || requestURI.startsWith(AppURL.SIGN_IN_URL) || requestURI.startsWith("/api/address/getCountries")
                || requestURI.startsWith("/getAllLichessProfiles")
                || (requestURI.matches(AppURL.CHESS_QUESTION_URL) && "POST".equals(httpMethod))
                || (requestURI.matches("/api/[^/]+/lichessAuth") && "GET".equals(httpMethod))
                || requestURI.startsWith(AppURL.PARTICIPANTS_URL) && "POST".equals(httpMethod);

        LOGGER.info("El valor devuelto por shouldNotFilter es: {}", value);

        return value;
    }
}
