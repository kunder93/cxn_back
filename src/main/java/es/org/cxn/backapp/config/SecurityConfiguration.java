
package es.org.cxn.backapp.config;

import es.org.cxn.backapp.filter.JwtRequestFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.NoArgsConstructor;

/**
 * Spring security configuration.
 *
 * @author Santiago Paz.
 *
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@NoArgsConstructor
public class SecurityConfiguration {

  /**
   * Bean fork enconde password.
   *
   * @return password enconded.
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Bean for managing user authentication.
   *
   * @param authConfig actual authentication configuration.
   * @return authentication manager.
   * @throws Exception when fails.
   */
  @Bean
  AuthenticationManager
        authenticationManager(final AuthenticationConfiguration authConfig)
              throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Filter chain applied to http petitions.
   *
   * @param http             the petition.
   * @param jwtRequestFilter the jwt filter.
   * @return object built.
   * @throws Exception if fails.
   */
  @Bean
  SecurityFilterChain filterChain(final HttpSecurity http, final @Autowired
  JwtRequestFilter jwtRequestFilter) throws Exception {
    // Disabled csrf for API REST
    http.csrf().disable().authorizeHttpRequests().requestMatchers("/**")
          .permitAll().requestMatchers("/*").permitAll().anyRequest()
          .authenticated().and().sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(
          jwtRequestFilter, UsernamePasswordAuthenticationFilter.class
    );

    return http.build();
  }

}
