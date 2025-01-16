
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

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuration class for setting up OpenAPI documentation.
 * <p>
 * This class configures the OpenAPI documentation for the Spring Boot
 * application, including the API's basic information such as title,
 * description, version, contact details, license, and available servers.
 * </p>
 *
 * @author Santiago Paz
 */
@Configuration
public class OpenApiConfig {

    /**
     * Default constructor for the OpenApiConfig class.
     * <p>
     * This constructor initializes the OpenApiConfig class, which is used to
     * configure the OpenAPI documentation for the Spring Boot application.
     * </p>
     */
    public OpenApiConfig() {
        // Default constructor
    }

    /**
     * Configures the OpenAPI bean with the API's metadata.
     * <p>
     * This method sets up the OpenAPI documentation for the application. It
     * includes details like the API's title, description, version, contact
     * information, license, and available servers (e.g., local and production
     * servers).
     * </p>
     *
     * @return a configured {@link OpenAPI} instance containing the API's metadata.
     */
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CXN API")
                        .description("This is a Spring Boot RESTful API using " + "springdoc-openapi and OpenAPI 3.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Your Name").email("xadreznaron@hotmail.com")
                                .url("https://xadreznaron.es"))
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
                .servers(List.of(new Server().url("https://xadreznaron.es:4443").description("Production server.")));
    }
}
