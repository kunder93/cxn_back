
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * An interceptor for logging HTTP requests and responses.
 * <p>
 * This interceptor logs the HTTP method, request URI before handling the
 * request, and the response status after the request has been processed.
 * </p>
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    /**
     * Logs the response status after the request has been processed.
     * <p>
     * This method logs the response status only if the INFO log level is enabled.
     * </p>
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param handler  the handler for the request
     * @param ex       any exception thrown during request processing
     * @throws Exception if an error occurs during response logging
     */
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception ex) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Response status: {}", response.getStatus());
        }
    }

    /**
     * Logs the HTTP method and request URI before handling the request.
     * <p>
     * This method logs the request details only if the INFO log level is enabled.
     * </p>
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param handler  the handler for the request
     * @return {@code true} to proceed with the request, {@code false} to abort
     * @throws Exception if an error occurs during request processing
     */
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Handling request: {} {}", request.getMethod(), request.getRequestURI());
        }
        return true;
    }
}
