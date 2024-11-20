package es.org.cxn.backapp.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.repository.UserEntityRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A custom filter to validate the user's enabled status for each request.
 * <p>
 * This filter ensures that any authenticated user making a request has their
 * "enabled" status checked in the database. If the user is not enabled or if
 * they do not exist in the database, the authentication context is cleared, and
 * an exception is thrown. This filter acts as an additional security measure to
 * restrict access to disabled users.
 * </p>
 */
@Component
public class EnableUserRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnableUserRequestFilter.class);

    /**
     * The user details service.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructs the filter with the required {@link UserEntityRepository}.
     *
     * @param userDetailsService the user details service
     */
    public EnableUserRequestFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Performs the filtering logic for each request.
     * <p>
     * This method checks if the current user is authenticated. If authenticated, it
     * fetches the user's details from the repository and ensures they are enabled.
     * If the user is not enabled or does not exist, an exception is thrown, and the
     * authentication context is cleared.
     * </p>
     *
     * @param request     the {@link ServletRequest} to process.
     * @param response    the {@link ServletResponse} to populate.
     * @param filterChain the {@link FilterChain} for delegating to the next filter.
     *
     * @throws IOException      if an input or output error occurs during the
     *                          filtering process.
     * @throws ServletException if an error occurs during the filtering process.
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LOGGER.info("EnableUserRequestFilter: Processing request.");
        String email;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
            LOGGER.info("Authentication detected for user: {}", email);
            if (!userDetailsService.loadUserByUsername(email).isEnabled()) {
                LOGGER.warn("User '{}' is disabled. Clearing security context.", email);
                SecurityContextHolder.clearContext();
                throw new IllegalStateException("User is disabled.");
            } else {
                LOGGER.info("User '{}' is enabled. Proceeding with request.", email);
            }
        } else {
            LOGGER.info("No authentication found. Proceeding without user validation.");
        }
        // Proceed with the next filter in the chain
        LOGGER.info("Request processing completed. Passing to the next filter.");
        filterChain.doFilter(request, response);
    }

    /**
     * Request that should not be filtered by jwt filter.
     */
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        final var requestURI = request.getRequestURI();
        final var httpMethod = request.getMethod();
        LOGGER.info("La URI en shouldNotFilter de ENABLE USER es: {}", requestURI);
        // Excluir la ruta /h2-console y otras rutas que no necesitan autenticación
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
