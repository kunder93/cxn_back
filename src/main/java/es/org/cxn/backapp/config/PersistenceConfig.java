
package es.org.cxn.backapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for persistence.
 *
 * @author Santiago Paz.
 *
 */
@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

    /**
     * Default constructor.
     */
    public PersistenceConfig() {
        super();
    }

}
