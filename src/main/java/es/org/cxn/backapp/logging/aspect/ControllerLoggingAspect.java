
package es.org.cxn.backapp.logging.aspect;

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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of controller methods.
 * <p>
 * This aspect logs the entry and exit of methods in the
 * {@code es.org.cxn.backapp.controller} package. It captures and logs method
 * names, parameters, return values, and exceptions.
 * </p>
 *
 * @author Santiago Paz
 */
@Component
@Aspect
public class ControllerLoggingAspect {

    /**
     * Logger instance for logging controller method executions.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    /**
     * Default constructor.
     */
    public ControllerLoggingAspect() {
        // Default constructor.
    }

    /**
     * Get the method name.
     *
     * @param signature the joinpoint signature.
     * @return the signature name.
     */
    private static String getMethodName(final Signature signature) {
        return signature.getName();
    }

    /**
     * Logs the normal exit of a method in the controller.
     * <p>
     * This advice is executed after a method in the
     * {@code es.org.cxn.backapp.controller} package returns successfully. It logs
     * the name of the method and the return value, if any.
     * </p>
     *
     * @param joinPoint the join point providing reflective access to the method
     *                  being executed
     * @param result    the return value of the method
     */
    @AfterReturning(pointcut = "execution(* es.org.cxn.backapp.controller.*.*(..))", returning = "result")
    public void logAfterReturning(final JoinPoint joinPoint, final Object result) {
        if (LOGGER.isInfoEnabled()) {
            final var methodName = getMethodName(joinPoint.getSignature());
            LOGGER.info("Exiting method: {} with return value: {}", methodName, result);
        }
    }

    /**
     * Logs the exit of a method in the controller when an exception is thrown.
     * <p>
     * This advice is executed after a method in the
     * {@code es.org.cxn.backapp.controller} package throws an exception. It logs
     * the name of the method and the exception's message.
     * </p>
     *
     * @param joinPoint the join point providing reflective access to the method
     *                  being executed
     * @param error     the exception thrown by the method
     */
    @AfterThrowing(pointcut = "execution(* es.org.cxn.backapp.controller.*.*(..))", throwing = "error")
    public void logAfterThrowing(final JoinPoint joinPoint, final Throwable error) {
        if (LOGGER.isErrorEnabled()) {
            final var methodName = getMethodName(joinPoint.getSignature());
            LOGGER.error("Exception in method: {} with message: {}", methodName, error.getMessage());
        }
    }
}
