
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
