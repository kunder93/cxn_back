/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package es.org.cxn.backapp.logging.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging aspect for services. Will log arguments and returned values.
 *
 * @author Santiago Paz.
 */
@Component
@Aspect
public class ServiceLoggingAspect {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServiceLoggingAspect.class);

    /**
     * Default constructor.
     */
    public ServiceLoggingAspect() {
        super();
    }

    /**
     * Logs the returned value after the method is called.
     *
     * @param joinPoint   point where the aspect is applied.
     * @param returnValue returned value.
     */
    @AfterReturning(value = "execution(* com.bernardomg.example.spring_mvc_react_maven_archetype_example..*Service*.*(..))", returning = "returnValue")
    public void afterCall(final JoinPoint joinPoint, final Object returnValue) {
        LOGGER.debug("Called {} and returning {}",
                joinPoint.getSignature().toShortString(), returnValue);
    }

    /**
     * Logs the received arguments before the method is called.
     *
     * @param joinPoint point where the aspect is applied.
     */
    @Before(value = "execution(* com.bernardomg.example.spring_mvc_react_maven_archetype_example..*Service*.*(..))", argNames = "joinPoint")
    public void beforeCall(final JoinPoint joinPoint) {
        LOGGER.debug("Calling {} with arguments {}",
                joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

}
