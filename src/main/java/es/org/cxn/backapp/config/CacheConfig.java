
package es.org.cxn.backapp.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Spring CacheConfig class.
 * <p>
 * This class enables caching within the Spring application using the
 * {@link EnableCaching} annotation. It allows methods and data to be cached
 * to improve performance and reduce redundant computations.
 * </p>
 *
 * <p>
 * The class is also annotated with {@link Configuration}, marking it as a
 * source of bean definitions for the application context.
 * </p>
 *
 * @author Santiago Paz
 */
@Configuration
@EnableCaching
public class CacheConfig {

  /**
   * Default constructor for CacheConfig.
   * <p>
   * This constructor allows Spring to instantiate the cache configuration
   * class.
   * No additional setup is required for basic caching functionality.
   * </p>
   */
  public CacheConfig() {
    // Default constructor
  }

}
