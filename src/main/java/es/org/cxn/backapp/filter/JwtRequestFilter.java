package es.org.cxn.backapp.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
 *
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * The jwt utilities.
     */
    @Autowired
    private DefaultJwtUtils jwtUtils;

    /**
     * The user details service.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Default constructor.
     */
    public JwtRequestFilter() {
        super();
    }

    /**
     * Modify filter actions.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        final var authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }
        if (username != null && SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            MyPrincipalUser user = (MyPrincipalUser) this.userDetailsService
                    .loadUserByUsername(username);
            Boolean jwtTokenValidation = jwtUtils.validateToken(jwt, user);
            if (Boolean.TRUE.equals(jwtTokenValidation)) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
