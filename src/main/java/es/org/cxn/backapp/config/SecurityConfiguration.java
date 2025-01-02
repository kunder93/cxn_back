package es.org.cxn.backapp.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.org.cxn.backapp.AppURL;
import es.org.cxn.backapp.filter.EnableUserRequestFilter;
import es.org.cxn.backapp.filter.JwtRequestFilter;

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
 * @author Santiago Paz
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    /**
     * Default constructor for SecurityConfiguration. This constructor is used to
     * create an instance of the SecurityConfiguration class.
     */
    public SecurityConfiguration() {
        // Default constructor
    }

    /**
     * Provides an authentication manager bean for managing user authentication.
     *
     * @param authConfig the current authentication configuration.
     * @return the authentication manager.
     * @throws Exception When fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures CORS settings for the application.
     *
     * @return the CORS configuration source.
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
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
     * @param http                    the HTTP security configuration.
     * @param jwtRequestFilter        the JWT filter.
     * @param enableUserRequestFilter The filter that check if user is or not
     *                                enabled.
     * @return the configured SecurityFilterChain.
     * @throws Exception The exception when fails.
     */
    @Bean
    public DefaultSecurityFilterChain filterChain(final HttpSecurity http,
            final @Autowired JwtRequestFilter jwtRequestFilter,
            final @Autowired EnableUserRequestFilter enableUserRequestFilter) throws Exception {
        LOGGER.info("Configurando SecurityFilterChain");
        // Disable CSRF for REST API and use stateless session management
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(withDefaults());

        // Allow H2 console access by modifying frame options
        http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(enableUserRequestFilter, UsernamePasswordAuthenticationFilter.class);
        // Permit all requests to /api/auth/signup and /api/auth/signin
        http.authorizeHttpRequests(requests -> requests.requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(AppURL.SIGN_UP_URL, AppURL.SIGN_IN_URL).permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs").permitAll()
                .requestMatchers(AppURL.CHESS_QUESTION_URL, AppURL.PARTICIPANTS_URL).permitAll()
                .requestMatchers("/v3/api-docs/swagger-config").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/*/lichessAuth").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/*/lichessAuth").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/activities").permitAll()
                .requestMatchers("/getAllLichessProfiles").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/lichessAuth").authenticated()
                .requestMatchers("/api/address/getCountries", "/api/address/country/**").permitAll().anyRequest()
                .authenticated());

        // Disable anonymous access
        http.anonymous(withDefaults());
        LOGGER.info("Autorizaciones configuradas para rutas específicas");
        return http.build();
    }

    /**
     * Provides a password encoder bean for encoding passwords using BCrypt.
     *
     * @return the password encoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures which web requests are ignored by Spring Security.
     * <p>
     * This method sets up a {@link WebSecurityCustomizer} that ignores requests to
     * the H2 console endpoint.
     * </p>
     *
     * @return the configured WebSecurityCustomizer.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

}
