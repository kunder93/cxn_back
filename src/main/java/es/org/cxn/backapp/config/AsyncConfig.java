package es.org.cxn.backapp.config;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration class to enable asynchronous method execution.
 * <p>
 * This configuration enables Spring's asynchronous method support using
 * {@link org.springframework.scheduling.annotation.Async}. It allows methods
 * annotated with {@code @Async} to run in a separate thread, facilitating
 * non-blocking execution.
 * </p>
 * <p>
 * Optional executor configurations can be added to this class if custom
 * behavior is required, such as defining thread pool sizes or naming threads.
 * </p>
 *
 */
@EnableAsync
@Configuration
public class AsyncConfig {
    /**
     * Default constructor.
     * <p>
     * Constructs a new instance of {@code AsyncConfig}. Required by Spring's
     * component scanning and context initialization for configuration classes.
     * </p>
     */
    public AsyncConfig() {
        // Default constructor for Spring context initialization
    }
}
