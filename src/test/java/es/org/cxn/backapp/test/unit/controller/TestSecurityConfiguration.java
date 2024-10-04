
package es.org.cxn.backapp.test.unit.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the test environment.
 * <p>
 * This class provides the security settings needed for testing purposes by
 * configuring Spring Security.
 * It disables CSRF protection, which is commonly not required for tests, and
 * allows all requests to pass without authentication.
 * Additionally, it sets the session management policy to stateless to ensure
 * that sessions are not maintained between requests.
 * </p>
 *
 * <p>
 * The configuration is suitable for unit tests or integration tests where
 * authentication and authorization are not required, simplifying test setups
 * and avoiding unnecessary complexity.
 * </p>
 *
 * @author Santi
 */
@Configuration
@EnableWebSecurity
public class TestSecurityConfiguration {

  /**
   * Configures the security filter chain for test environments.
   * <p>
   * This configuration disables CSRF protection, allows all requests
   * without authentication,
   * and sets the session management to stateless.
   * </p>
   *
   * @param http the {@link HttpSecurity} to configure
   * @return the configured {@link SecurityFilterChain}
   * @throws Exception if any configuration error occurs
   */
  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http)
        throws Exception {
      http.csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(
                      authorizeRequests -> authorizeRequests
                              // Allow all requests to pass without authentication
                              .requestMatchers("/**").permitAll()
              ).sessionManagement(management -> management
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
