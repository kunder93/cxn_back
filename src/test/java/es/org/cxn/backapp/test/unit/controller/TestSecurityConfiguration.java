
package es.org.cxn.backapp.test.unit.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class TestSecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
    http.csrf().disable()
          .authorizeRequests(
                authorizeRequests -> authorizeRequests
                      // Allow all requests to pass without authentication
                      .requestMatchers("/**").permitAll()
          ).sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }
}
