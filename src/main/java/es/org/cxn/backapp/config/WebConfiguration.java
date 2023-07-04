
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
 *
 * @author Santiago Paz.
 *
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  /**
   * Main constructor.
   */
  public WebConfiguration() {
    super();
  }

  @Override
  public final void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("*");
  }

  /**
   * Add view controllers.
   */
  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
    registry.addViewController("/notFound").setViewName("forward:/");
  }

  /**
   * Add error page.
   *
   * @return container with error page added.
   */
  @Bean
  WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
        containerCustomizer() {
    return (var container) -> container
          .addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"));

  }

}
