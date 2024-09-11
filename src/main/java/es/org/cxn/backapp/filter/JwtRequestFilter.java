
package es.org.cxn.backapp.filter;

import es.org.cxn.backapp.service.DefaultJwtUtils;
import es.org.cxn.backapp.service.MyPrincipalUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  /**
   * The logger.
   */
  private static final Logger LOGGER =
        LoggerFactory.getLogger(JwtRequestFilter.class);

  /**
   * Token spaces length for bearer token.
   */
  private static final int TOKEN_SPACES = 7;

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

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
            "Encabezado de autorización recibido: {}", authorizationHeader
      );
    }

    if (authorizationHeader != null
          && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(TOKEN_SPACES);
      username = DefaultJwtUtils.extractUsername(jwt);

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Token JWT extraído: {}", jwt);
        LOGGER.info("Nombre de usuario extraído del token JWT: {}", username);
      }
    }

    if (username != null) {
      if (LOGGER.isInfoEnabled()) {
        LOGGER.info(
              "Autenticación actual en SecurityContextHolder: {}",
              SecurityContextHolder.getContext().getAuthentication()
        );
      }

      var user = (MyPrincipalUser) this.userDetailsService
            .loadUserByUsername(username);

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Usuario cargado: {}", user.getUsername());
      }

      var jwtTokenValidation = DefaultJwtUtils.validateToken(jwt, user);

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info(
              "Resultado de la validación del token: {}", jwtTokenValidation
        );
      }

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

        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("Autenticación actualizada en SecurityContextHolder");
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
