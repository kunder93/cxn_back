
package es.org.cxn.backapp.config;

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
   * Configures the OpenAPI bean with the API's metadata.
   * <p>
   * This method sets up the OpenAPI documentation for the application.
   * It includes details like the API's title, description, version, contact
   * information, license, and available servers (e.g., local and
   * production servers).
   * </p>
   *
   * @return a configured {@link OpenAPI} instance containing the API's
   * metadata.
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
          .info(
                new Info().title("CXN API")
                      .description(
                            "This is a Spring Boot RESTful API using "
                                  + "springdoc-openapi and OpenAPI 3."
                      ).version("v1.0.0")
                      .contact(
                            new Contact().name("Your Name")
                                  .email("xadreznaron@hotmail.com")
                                  .url("https://xadreznaron.es")
                      )
                      .license(
                            new License().name("MIT License")
                                  .url("https://opensource.org/licenses/MIT")
                      )
          )
          .servers(
                List.of(
                      new Server().url("https://xadreznaron.es:4443")
                            .description("Production server.")
                )
          );
  }
}
