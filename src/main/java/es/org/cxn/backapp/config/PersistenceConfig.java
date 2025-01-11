
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
