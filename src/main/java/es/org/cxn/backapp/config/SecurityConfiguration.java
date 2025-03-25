package es.org.cxn.backapp.config;

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

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.ott.OneTimeTokenLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.filter.JwtRequestFilter;
import es.org.cxn.backapp.security.DefaultJwtUtils;

/**
 * Security configuration for the application.
 * <p>
 * This class configures Spring Security settings, including JWT authentication,
 * CORS policies, password encoding, and access control for various endpoints.
 *
 * <p>
 * The configuration includes:
 * <ul>
 * <li>Disabling CSRF protection (as JWT is used).</li>
 * <li>Stateless session management.</li>
 * <li>CORS configuration.</li>
 * <li>Custom JWT authentication filter.</li>
 * <li>Access control for API endpoints.</li>
 * </ul>
 *
 * @author Santiago Paz
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    /**
     * Logger for logging security-related messages.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    /**
     * Utility class for working with JWT tokens. Provides methods for token
     * validation and user extraction.
     */
    private final DefaultJwtUtils jwtUtils;

    /**
     * Service to load user details based on the username from the authentication
     * request.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for SecurityConfiguration.
     *
     * @param jwtUtils           Utility class for handling JWT tokens.
     * @param userDetailsService Service for loading user details.
     */
    public SecurityConfiguration(final DefaultJwtUtils jwtUtils, final UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Provides the authentication manager.
     *
     * @param authConfig The authentication configuration.
     * @return The authentication manager instance.
     * @throws Exception if an error occurs while retrieving the authentication
     *                   manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings.
     *
     * @return A configured {@link UrlBasedCorsConfigurationSource} instance.
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        LOGGER.info("Configuring CORS");
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configures the security filter chain, including JWT filters and access rules.
     *
     * @param http The {@link HttpSecurity} instance.
     * @return The configured security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public DefaultSecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        LOGGER.info("Configuring Security Filter Chain");

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(withDefaults()).headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", AppURL.SIGN_UP_URL, AppURL.SIGN_IN_URL, "/swagger-ui/**",
                                "/v3/api-docs/**", AppURL.CHESS_QUESTION_URL, AppURL.PARTICIPANTS_URL,
                                "/api/activities", "/api/activities/*/image", "/api/address/**")
                        .permitAll().requestMatchers(HttpMethod.GET, "/api/*/lichessAuth").permitAll()
                        .requestMatchers("/api/ott/my-generate-url").permitAll()
                        .requestMatchers("/api/ott/my-generate-url/password/reset").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/lichessAuth").authenticated().anyRequest()
                        .authenticated())
                .oneTimeTokenLogin((OneTimeTokenLoginConfigurer<HttpSecurity> ott) -> {
                    ott.tokenGeneratingUrl("/api/ott/my-generate-url");
                    ott.showDefaultSubmitPage(false);
                });
        return http.build();
    }

    /**
     * Creates a JWT authentication filter.
     *
     * @return An instance of {@link JwtRequestFilter}.
     */
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtils, userDetailsService);
    }

    /**
     * Provides a password encoder bean using BCrypt.
     *
     * @return A {@link BCryptPasswordEncoder} instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures web security to ignore specific endpoints such as H2 Console and
     * API documentation.
     *
     * @return A {@link WebSecurityCustomizer} instance.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/v3/api-docs/**"), new AntPathRequestMatcher("/swagger-ui/**"));
    }
}
