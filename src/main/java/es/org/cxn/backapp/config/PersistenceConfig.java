
package es.org.cxn.backapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for persistence.
 * <p>
 * This class enables transaction management and is part of the persistence
 * configuration for the application.
 * </p>
 *
 * <p>
 * It is annotated with {@link EnableTransactionManagement} to ensure that
 * transactional behavior is properly handled, and with {@link Configuration}
 * to indicate that it contains Spring configuration beans.
 * </p>
 *
 * @author Santiago Paz
 */
@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

  /**
   * Default constructor for PersistenceConfig.
   * <p>
   * This constructor allows Spring to instantiate the persistence
   * configuration class. No additional setup is required.
   * </p>
   */
  public PersistenceConfig() {
    // Default constructor
  }
}
