package es.org.cxn.backapp.filter;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.security.DefaultJwtUtils;
import es.org.cxn.backapp.security.MyPrincipalUser;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to process HTTP requests for JWT token-based authentication.
 * <p>
 * This filter intercepts requests to validate the presence and validity of a
 * JSON Web Token (JWT) in the "Authorization" header. If the token is valid, it
 * extracts the username and sets the authentication context. Requests to
 * specific unprotected URIs or HTTP methods are bypassed.
 * </p>
 *
 * <p>
 * This filter extends
 * {@link org.springframework.web.filter.OncePerRequestFilter}, ensuring it is
 * executed once per request.
 * </p>
 *
 * @author Santiago Paz
 * @version 1.0
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * Logger instance for recording debug and error information.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    /**
     * Prefix used in the Authorization header to indicate a Bearer token.
     */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Length of the Bearer token prefix, used for extracting the JWT from the
     * header.
     */
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    /**
     * Name of the HTTP header that contains the authorization information.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Service for retrieving user details, used to validate and load user
     * information during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructs a new JwtRequestFilter.
     *
     * @param usrDetailsService the service used to load user details by username.
     */
    public JwtRequestFilter(final UserDetailsService usrDetailsService) {
        super();
        this.userDetailsService = usrDetailsService;
    }

    /**
     * Filters incoming HTTP requests to validate JWT tokens.
     * <p>
     * Extracts the JWT from the "Authorization" header, validates it, and sets the
     * Spring Security authentication context if valid.
     * </p>
     *
     * @param request     the HTTP request.
     * @param response    the HTTP response.
     * @param filterChain the filter chain to pass the request and response.
     * @throws ServletException if an error occurs during filtering.
     * @throws IOException      if an input or output error occurs.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();

        // Ensure debug logging is enabled before calling LOGGER.debug
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Request URI: {}", requestURI);
        }

        try {
            extractJwtFromRequest(request).flatMap(this::getUsernameFromJwt)
                    .flatMap(username -> validateAndLoadUser(username, request))
                    .ifPresent(user -> setAuthentication(user, request));
        } catch (JwtException e) {
            // Ensure warn logging is enabled before calling LOGGER.warn
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("JWT validation failed: {}", e.getMessage());
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the "Authorization" header of the HTTP request.
     *
     * @param request the HTTP request.
     * @return an {@link Optional} containing the JWT token if present and valid;
     *         otherwise, an empty {@link Optional}.
     */
    private Optional<String> extractJwtFromRequest(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        Optional<String> jwtToken = Optional.empty(); // Default to empty
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            jwtToken = Optional.of(authorizationHeader.substring(BEARER_PREFIX_LENGTH));
        } else {
            LOGGER.debug("Missing or invalid Authorization header");
        }
        return jwtToken;
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param jwt the JWT token.
     * @return an {@link Optional} containing the username if successfully
     *         extracted; otherwise, an empty {@link Optional}.
     */
    private Optional<String> getUsernameFromJwt(final String jwt) {
        final Optional<String> username; // Default to empty
        username = Optional.ofNullable(DefaultJwtUtils.extractUsername(jwt));
        return username;
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
     * Sets the Spring Security authentication context with the specified user.
     *
     * @param user    the authenticated user.
     * @param request the HTTP request.
     */
    private void setAuthentication(final MyPrincipalUser user, final HttpServletRequest request) {
        final var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Guarded log statement
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Authentication set for user: {}", user.getUsername());
        }
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
        LOGGER.debug("Skipping JWT filter: {}", shouldSkip);
        return shouldSkip;
    }

    /**
     * Validates the JWT token and loads the corresponding user details.
     *
     * @param username the username extracted from the JWT token.
     * @param request  the HTTP request.
     * @return an {@link Optional} containing the user details if validation
     *         succeeds; otherwise, an empty {@link Optional}.
     */
    private Optional<MyPrincipalUser> validateAndLoadUser(final String username, final HttpServletRequest request) {
        Optional<MyPrincipalUser> result = Optional.empty(); // Single exit point, initialize the result

        final MyPrincipalUser user = (MyPrincipalUser) userDetailsService.loadUserByUsername(username);
        final String jwt = extractJwtFromRequest(request).orElse(null);
        if (Boolean.TRUE.equals(DefaultJwtUtils.validateToken(jwt, user))) {
            LOGGER.debug("Valid JWT for user: {}", username);
            result = Optional.of(user);
        } else {
            LOGGER.debug("Invalid JWT for user: {}", username);
        }

        return result; // The single return statement
    }

}
