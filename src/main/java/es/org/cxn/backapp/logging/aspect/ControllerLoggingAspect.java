
package es.org.cxn.backapp.logging.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Logging aspect for controllers. Logs method entry and exit, along with
 * method parameters and return values.
 *
 * <p>This aspect is applied to all methods in classes under the
 * es.org.cxn.backapp.controller package. It logs method entry and exit,
 * including the method name and parameters.</p>
 *
 * @author Santiago Paz
 */
@Component
@Aspect
public class ControllerLoggingAspect {

  /**
   * Logger instance to log messages.
   */
  private static final Logger LOGGER =
        LoggerFactory.getLogger(ControllerLoggingAspect.class);

  /**
   * Default constructor.
   */
  public ControllerLoggingAspect() {
    // Default empty constructor.
  }

  /**
   * Logs the entry of a method in the controller.
   *
   * <p>This advice is executed before any method in the
   * es.org.cxn.backapp.controller package. It logs the name of the method
   * being called and its parameters.</p>
   *
   * @param joinPoint The join point providing reflective access to the
   * method being called.
   * @param param1 The first parameter of the method being logged.
   */
  @Before("execution(* es.org.cxn.backapp.controller.*.*(..))")
  public void logBefore(JoinPoint joinPoint) {
    var methodName = getMethodName(joinPoint);
    var args = joinPoint.getArgs();
    LOGGER.info("Entering method: {} with arguments: {}", methodName, args);
  }

  /**
   * Logs the exit of a method in the controller.
   *
   * <p>This advice is executed after a method in the
   * es.org.cxn.backapp.controller package returns. It logs the name of the
   * method and its return value.</p>
   *
   * @param joinPoint The join point providing reflective access to the
   * method being executed.
   * @param param1 The first parameter of the method being logged.
   */
  @AfterReturning("execution(* es.org.cxn.backapp.controller.*.*(..))")
  public void logAfter(JoinPoint joinPoint) {
    var methodName = getMethodName(joinPoint);
    LOGGER.info("Exiting method: {}", methodName);
  }

  @AfterReturning(
        pointcut = "execution(* es.org.cxn.backapp.controller.*.*(..))",
        returning = "result"
  )
  public void logAfterReturning(JoinPoint joinPoint, Object result) {
    var methodName = getMethodName(joinPoint);
    LOGGER.info("Exiting method: {} with return value: {}", methodName, result);
  }

  @AfterThrowing(
        pointcut = "execution(* es.org.cxn.backapp.controller.*.*(..))",
        throwing = "error"
  )
  public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
    var methodName = getMethodName(joinPoint);
    LOGGER.error(
          "Exception in method: {} with message: {}", methodName,
          error.getMessage()
    );
  }

  /**
   * Helper method to retrieve the name of the method being executed.
   *
   * @param joinPoint The join point providing reflective access to the
   * method.
   * @return The name of the method being executed.
   */
  private static String getMethodName(final JoinPoint joinPoint) {
    final var signature = joinPoint.getSignature();
    return signature.getName();
  }
}
