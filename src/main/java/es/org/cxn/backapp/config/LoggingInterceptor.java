
package es.org.cxn.backapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER =
        LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler
  ) throws Exception {
    LOGGER.info(
          "Handling request: {} {}", request.getMethod(),
          request.getRequestURI()
    );
    return true;
  }

  @Override
  public void afterCompletion(
        HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex
  ) throws Exception {
    LOGGER.info("Response status: {}", response.getStatus());
  }
}
