
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
   * Main void constructor.
   */
  public ControllerLoggingAspect() {

  }

  /**
   * Log before.
   *
   * @param joinPoint The joinPoint
   * @param param1 The param1.
   */
  @Before("execution(* es.org.cxn.backapp.controller.*.*(..)) && args(param1)")
  public void logBefore(final JoinPoint joinPoint, final Object param1) {
    final var methodName = getMethodName(joinPoint);
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
  public void logMethodExit(final JoinPoint joinPoint, final Object param1) {
    final var methodName = getMethodName(joinPoint);
    LOGGER.info(
          "Exiting AUTH CONTROLLER method: {} Parameters: {}", methodName,
          param1
    );
  }

  /**
   * Helper method to get method name from join point.
   *
   * @param joinPoint The joinPoint.
   * @return The method name.
   */
  private String getMethodName(final JoinPoint joinPoint) {
    final var signature = joinPoint.getSignature();
    return signature.getName();
  }
}
