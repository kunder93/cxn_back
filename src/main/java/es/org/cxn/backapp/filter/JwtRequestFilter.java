
package es.org.cxn.backapp.filter;

import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.MyPrincipalUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter http request looking for jwt token and checking if can authenticate.
 *
 * @author Santiago Paz.
 *
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  /**
   * Token spaces length for bearer token.
   */
  private static final int TOKEN_SPACES = 7;

  /**
   * The jwt utilities.
   */
  private DefaultJwtUtils jwtUtils;

  /**
   * The user details service.
   */
  private UserDetailsService userDetailsService;

  /**
   * Constructor for JwtRequestFilter.
   *
   * @param defaultJwtUtils the JWT utilities
   * @param usrDetService the user details service
   */
  public JwtRequestFilter(
        final DefaultJwtUtils defaultJwtUtils,
        final UserDetailsService usrDetService
  ) {
    super();
    this.jwtUtils = defaultJwtUtils;
    this.userDetailsService = usrDetService;
  }

  /**
   * Modify filter actions.
   */
  @Override
  protected void doFilterInternal(
        final HttpServletRequest request, final HttpServletResponse response,
        final FilterChain filterChain
  ) throws ServletException, IOException {
    final var authorizationHeader = request.getHeader("Authorization");
    String username = null;
    String jwt = null;

    if (authorizationHeader != null
          && authorizationHeader.startsWith("Bearer")) {
      jwt = authorizationHeader.substring(TOKEN_SPACES);
      username = jwtUtils.extractUsername(jwt);
    }
    if (username != null
          && SecurityContextHolder.getContext().getAuthentication() == null) {
      var user = (MyPrincipalUser) this.userDetailsService
            .loadUserByUsername(username);
      var jwtTokenValidation = jwtUtils.validateToken(jwt, user);
      if (Boolean.TRUE.equals(jwtTokenValidation)) {
        var usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
              );
        usernamePasswordAuthenticationToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext()
              .setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    filterChain.doFilter(request, response);
  }

}
