
package es.org.cxn.backapp.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Spring CacheConfig class.
 *
 * @author Santiago Paz
 *
 */
@Configuration
@EnableCaching
public class CacheConfig {

  /**
   * Main empty constructor.
   */
  public CacheConfig() {
    super();
  }

}
