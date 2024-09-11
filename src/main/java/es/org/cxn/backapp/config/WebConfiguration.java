
package es.org.cxn.backapp.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web Configuration.
 * <p>
 * This class configures the web application, including CORS mappings,
 * view controllers, and error page handling.
 * </p>
 *
 * <p>
 * This configuration is automatically detected by Spring Boot because it is
 * annotated with {@link Configuration}, and it implements
 * {@link WebMvcConfigurer}.
 * </p>
 *
 * @author Santiago Paz
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  /**
   * Default constructor for WebConfiguration.
   * <p>
   * This constructor is required for the Spring container to instantiate the
   * class as part of its configuration. It doesn't require any parameters or
   * additional setup.
   * </p>
   */
  public WebConfiguration() {
    // Default constructor
  }

  @Override
  public final void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("*");
  }

  /**
   * Adds view controllers for handling specific routes.
   * <p>
   * This example forwards requests to "/notFound" to the root ("/").
   * </p>
   */
  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
    registry.addViewController("/notFound").setViewName("forward:/");
  }

  /**
   * Configures the web server to handle a 404 (NOT_FOUND) error by forwarding
   * it to the "/notFound" view.
   *
   * @return A customizer that adds an error page for HTTP status 404.
   */
  @Bean
  WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
        containerCustomizer() {
    return container -> container
          .addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"));
  }
}
