package es.org.cxn.backapp.config;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for JWT properties.
 * <p>
 * This class loads JWT-related properties from the application's configuration
 * files (e.g., {@code application.properties} or {@code application.yml}).
 * </p>
 *
 * <p>
 * Example properties:
 * </p>
 *
 * <pre>
 * jwt.secret=your-secret-key
 * jwt.expiration=3600
 * </pre>
 *
 * <p>
 * These properties are injected using Spring Boot's
 * {@code @ConfigurationProperties} mechanism.
 * </p>
 *
 * @author Santiago Paz
 * @version 1.0
 * @since 2024-02-02
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * The secret key used for signing and verifying JWT tokens.
     */
    private String secret;

    /**
     * The expiration time of the JWT token in seconds.
     */
    private int expiration;

    /**
     * Default constructor for JwtProperties.
     * <p>
     * This constructor is required for Spring Boot's
     * {@code @ConfigurationProperties} mechanism to work properly.
     * </p>
     */
    public JwtProperties() {
        // Default constructor
    }

    /**
     * Gets the expiration time of the JWT token.
     *
     * @return the expiration time in seconds.
     */
    public int getExpiration() {
        return expiration;
    }

    /**
     * Gets the secret key used for signing and verifying JWT tokens.
     *
     * @return the secret key as a string.
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets the expiration time of the JWT token.
     *
     * @param exp the expiration time in seconds.
     */
    public void setExpiration(final int exp) {
        expiration = exp;
    }

    /**
     * Sets the secret key used for signing and verifying JWT tokens.
     *
     * @param secr the secret key as a string.
     */
    public void setSecret(final String secr) {
        secret = secr;
    }
}
