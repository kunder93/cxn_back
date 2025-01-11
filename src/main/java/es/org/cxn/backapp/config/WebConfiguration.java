
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

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web Configuration.
 * <p>
 * This class configures the web application, including CORS mappings, view
 * controllers, and error page handling.
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
     * The logging intercepter.
     */
    private final LoggingInterceptor loggingInterceptor;

    /**
     * Constructs a {@code WebConfiguration} instance with the specified
     * {@code LoggingInterceptor}.
     * <p>
     * This constructor is used to inject a {@code LoggingInterceptor} into the
     * configuration to handle logging of HTTP requests and responses.
     * </p>
     *
     * @param value the interceptor used for logging HTTP requests and responses.
     *              This object is typically injected by the Spring framework.
     */
    public WebConfiguration(final LoggingInterceptor value) {
        loggingInterceptor = value;
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings.
     *
     * <p>
     * This method allows any origin and any HTTP method to access resources within
     * the application, which is suitable for scenarios where the backend serves
     * clients from different origins.
     * </p>
     *
     * @param registry the {@link CorsRegistry} used to specify the CORS mappings
     */
    @Override
    public final void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    /**
     * Adds an interceptor for logging requests.
     *
     * <p>
     * This method registers the {@code loggingInterceptor} to be applied to all
     * incoming requests. Interceptors are useful for cross-cutting concerns such as
     * logging, authentication, or modifying requests.
     * </p>
     *
     * @param registry the {@link InterceptorRegistry} used to add the interceptor
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
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
     * Configures the web server to handle a 404 (NOT_FOUND) error by forwarding it
     * to the "/notFound" view.
     *
     * @return A customizer that adds an error page for HTTP status 404.
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notFound"));
    }
}
