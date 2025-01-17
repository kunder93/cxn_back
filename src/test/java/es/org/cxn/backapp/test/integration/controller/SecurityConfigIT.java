
package es.org.cxn.backapp.test.integration.controller;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
public class SecurityConfigIT {

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
    SecurityFilterChain filterChain(final HttpSecurity http)
            throws Exception {
      // Desactiva la seguridad para las pruebas
      http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
              .sessionManagement(management -> management
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
