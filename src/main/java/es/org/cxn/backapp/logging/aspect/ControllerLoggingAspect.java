/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.logging.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Logging aspect for controllers. Will log arguments and returned values.
 *
 * @author Santiago Paz.
 */
@Component
@Aspect
public class ControllerLoggingAspect {

  /**
   * The logger.
   */
  private static final Logger LOGGER =
        LoggerFactory.getLogger(ControllerLoggingAspect.class);

  /**
   * Log before.
   *
   * @param joinPoint The joinPoint
   * @param param1 The param1.
   */
  @Before("execution(* es.org.cxn.backapp.controller.*.*(..)) && args(param1)")
  public void logBefore(JoinPoint joinPoint, Object param1) {
    var methodName = joinPoint.getSignature().getName();
    LOGGER.info(
          "Entering AUTH CONTROLLER method: {} Parameters: {}", methodName,
          param1
    );
  }

  /**
   * Log after.
   *
   * @param joinPoint The joinPoint.
   * @param param1 The param1.
   */
  @AfterReturning(
    "execution(* es.org.cxn.backapp.controller.*.*(..)) && args(param1)"
  )
  public void logMethodExit(JoinPoint joinPoint, Object param1) {
    var methodName = joinPoint.getSignature().getName();
    LOGGER.info(
          "Exiting AUTH CONTROLLER method: {} Parameters: {}", methodName,
          param1
    );
  }

}
