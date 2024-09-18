
package es.org.cxn.backapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for Aspect-Oriented Programming (AOP) in Spring.
 * <p>
 * This class enables AspectJ auto-proxying, which is used to apply
 * aspect-oriented programming (AOP) to beans in the Spring application context.
 * </p>
 *
 * @author Santi
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

  /**
   * Default empty constructor.
   */
  public AspectConfig() {
    // Default constructor
  }
}
