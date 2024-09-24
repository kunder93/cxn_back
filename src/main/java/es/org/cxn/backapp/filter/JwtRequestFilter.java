
package es.org.cxn.backapp.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.MyPrincipalUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter http request looking for jwt token and checking if can authenticate.
 *
 * @author Santiago Paz.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    /**
     * Token spaces length for bearer token.
     */
    private static final int TOKEN_SPACES = 7;

    /**
     * The user details service.
     */
    final private UserDetailsService userDetailsService;

    /**
     * Constructor for JwtRequestFilter.
     *
     * @param usrDetService the user details service
     */
    public JwtRequestFilter(final UserDetailsService usrDetService) {
        super();
        this.userDetailsService = usrDetService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        LOGGER.info("Processing request URI: {}", requestURI);

        boolean shouldAuthenticate = true; // Flag to control authentication process
        final String authorizationHeader = request.getHeader("Authorization");

        // Check if the authorization header is valid
        if (!isValidAuthorizationHeader(authorizationHeader)) {
            shouldAuthenticate = false;
        }

        String jwt = null;
        String username = null;
        if (shouldAuthenticate) {
            jwt = authorizationHeader.substring(TOKEN_SPACES);
            username = DefaultJwtUtils.extractUsername(jwt);

            // If no username is found, stop authentication
            if (username == null) {
                shouldAuthenticate = false;
            }
        }

        MyPrincipalUser user = null;
        if (shouldAuthenticate) {
            user = (MyPrincipalUser) this.userDetailsService.loadUserByUsername(username);
            if (Boolean.FALSE.equals(DefaultJwtUtils.validateToken(jwt, user))) {
                shouldAuthenticate = false;
            }
        }

        // Set authentication if valid, otherwise proceed without
        if (shouldAuthenticate) {
            setAuthentication(user, request);
        }

        // Proceed with the filter chain regardless of authentication status
        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthorizationHeader(final String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private void setAuthentication(final MyPrincipalUser user, final HttpServletRequest request) {
        final var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /**
     * Request that should not be filtered by jwt filter.
     */
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        final var requestURI = request.getRequestURI();
        final var httpMethod = request.getMethod();
        LOGGER.info("La URI en shouldNotFilter es: {}", requestURI);
        // Excluir la ruta /h2-console y otras rutas que no necesitan autenticación
        final var value = requestURI.startsWith("/h2-console") || requestURI.startsWith(AppURL.SIGN_UP_URL)
                || requestURI.startsWith(AppURL.SIGN_IN_URL) || requestURI.startsWith("/api/address/getCountries")
                || (requestURI.startsWith(AppURL.CHESS_QUESTION_URL) && "POST".equals(httpMethod))
                || (requestURI.matches("/api/[^/]+/lichessAuth") && "GET".equals(httpMethod))
                || requestURI.startsWith(AppURL.PARTICIPANTS_URL) && "POST".equals(httpMethod);

        LOGGER.info("El valor devuelto por shouldNotFilter es: {}", value);

        return value;
    }
}
