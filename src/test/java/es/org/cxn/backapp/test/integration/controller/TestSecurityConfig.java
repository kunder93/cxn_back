
package es.org.cxn.backapp.test.integration.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad personalizada para pruebas de integración.
 * <p>
 * Esta configuración desactiva la seguridad de Spring Security durante la
 * ejecución de las pruebas de integración. Se utiliza para permitir el acceso
 * sin restricciones a todas las solicitudes HTTP, evitando problemas
 * relacionados con la seguridad durante las pruebas.
 * </p>
 *
 * <p>
 * La configuración desactiva la protección CSRF y establece la política de
 * creación de sesiones en {@link SessionCreationPolicy#STATELESS}, lo que
 * indica que las sesiones no se utilizan.
 * </p>
 *
 * @author Santi
 */
@TestConfiguration
public class TestSecurityConfig {

  /**
   * Configura el {@link SecurityFilterChain} para desactivar la seguridad
   * durante las pruebas.
   * <p>
   * Esta configuración desactiva la protección CSRF, permite todas las
   * solicitudes sin restricciones y establece la política de creación de
   * sesiones como sin estado.
   * </p>
   *
   * @param http La instancia de {@link HttpSecurity} utilizada para configurar
   * la seguridad.
   * @return La instancia de {@link SecurityFilterChain} construida con la
   * configuración de seguridad.
   * @throws Exception Si ocurre un error durante la configuración de seguridad.
   */
  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http)
        throws Exception {
      // Desactiva la seguridad para las pruebas
      http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
              .sessionManagement(management -> management
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
