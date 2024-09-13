
package es.org.cxn.backapp.config;

import es.org.cxn.backapp.filter.JwtRequestFilter;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring security configuration.
 * <p>
 * This class configures the security settings of the application, including
 * CORS, JWT filters, password encoding, and authentication management.
 * </p>
 *
 * <p>
 * The {@link EnableWebSecurity} annotation enables Spring Security’s web
 * security support, and the {@link EnableMethodSecurity} annotation allows
 * method-level security with pre/post annotations.
 * </p>
 *
 * @author Santiago Paz.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

  private static final Logger LOGGER =
        LoggerFactory.getLogger(SecurityConfiguration.class);

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
          .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
  }

  /**
   * Configures CORS settings for the application.
   *
   * @return the CORS configuration source.
   */
  @Bean
  /* default */ CorsConfigurationSource corsConfigurationSource() {
    LOGGER.info("Configurando CORS");
    final var configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));

    final var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

  /**
   * Configures the security filter chain applied to HTTP requests.
   *
   * @param http             the HTTP security configuration.
   * @param jwtRequestFilter the JWT filter.
   * @return the configured SecurityFilterChain.
   * @throws Exception
   */
  @Bean
  SecurityFilterChain filterChain(final HttpSecurity http, final @Autowired
  JwtRequestFilter jwtRequestFilter) throws Exception {
    LOGGER.info("Configurando SecurityFilterChain");

    // Disable CSRF for REST API and use stateless session management
    http.csrf().disable().sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors();

    // Allow H2 console access by modifying frame options
    http.headers().frameOptions().sameOrigin();

    // Add JWT filter before UsernamePasswordAuthenticationFilter
    http.addFilterBefore(
          jwtRequestFilter, UsernamePasswordAuthenticationFilter.class
    );

    // Permit all requests to /api/auth/signup and /api/auth/signin
    http.authorizeHttpRequests().requestMatchers("/h2-console/**").permitAll()
          .requestMatchers("/api/auth/signup", "/api/auth/signinn").permitAll()
          .requestMatchers("/swagger-ui-custom.html.", "v3/api-docs")
          .permitAll()
          .requestMatchers(
                "/api/auth/signup", "/api/auth/signin",
                "/api/address/getCountries", "/api/address/country/**"
          ).permitAll().anyRequest().authenticated();
    http.headers().frameOptions().sameOrigin();
    // Permit all requests to /api/auth/signup, /api/auth/signin,
    // and the AddressController endpoints

    // Disable anonymous access
    http.anonymous();
    LOGGER.info("Autorizaciones configuradas para rutas específicas");
    return http.build();
  }

  /**
   * Provides a password encoder bean for encoding passwords using BCrypt.
   *
   * @return the password encoder.
   */
  @Bean
  /* default */ PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Provides an authentication manager bean for managing user authentication.
   *
   * @param authConfig the current authentication configuration.
   * @return the authentication manager.
   */
  @Bean
  /* default */ AuthenticationManager
        authenticationManager(final AuthenticationConfiguration authConfig)
              throws Exception {
    return authConfig.getAuthenticationManager();
  }

}
