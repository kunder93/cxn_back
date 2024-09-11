
package es.org.cxn.backapp.controller;

import es.org.cxn.backapp.response.DefaultResponse;
import es.org.cxn.backapp.response.Response;
import es.org.cxn.backapp.response.ResponseStatus;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Captures and handles exceptions for all the controllers.
 *
 * @author Santiago Paz.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Default constructor.
   */
  public GlobalExceptionHandler() {
    super();
  }

  /**
   * Handle response exceptions.
   *
   * @param exception the exception.
   * @param request   the request.
   * @return Response entity.
   */
  @ExceptionHandler({ RuntimeException.class })
  public final ResponseEntity<Object> handleExceptionDefault(
        final Exception exception, final WebRequest request
  ) {
    final var headers = new HttpHeaders();
    final var status = HttpStatus.INTERNAL_SERVER_ERROR;

    return handleExceptionInternal(exception, null, headers, status, request);
  }

  /**
   * Response entity.
   */
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
        final Exception exception, final Object body, final HttpHeaders headers,
        final HttpStatusCode status, final WebRequest request
  ) {
    final Response<String> response;
    final var message =
          (exception.getMessage() == null) ? "" : exception.getMessage();

    response = new DefaultResponse<>(message, ResponseStatus.FAILURE);

    return super.handleExceptionInternal(
          exception, response, headers, status, request
    );
  }

  /**
   * Response entity argument not valid.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException exception,
        final HttpHeaders headers, final HttpStatusCode status,
        final WebRequest request
  ) {
    final Iterable<String> errors =
          exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

    final Response<Iterable<String>> response =
          new DefaultResponse<>(errors, ResponseStatus.WARNING);

    return super.handleExceptionInternal(
          exception, response, headers, status, request
    );
  }

  /**
   * Handle AccessDeniedException.
   *
   * @param exception the exception.
   * @param request   the request.
   * @return Response entity with forbidden status.
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Response<String>> handleAccessDeniedException(
        final AccessDeniedException exception, final WebRequest request
  ) {
    final Response<String> response =
          new DefaultResponse<>(exception.getMessage(), ResponseStatus.FAILURE);
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  /**
   * Handle Throwable for authentication-related issues.
   *
   * @param throwable the throwable (more general than exception).
   * @param request   the request.
   * @return Response entity with unauthorized status.
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Response<String>>
        handleThrowable(final Throwable throwable, final WebRequest request) {
    final Response<String> response =
          new DefaultResponse<>(throwable.getMessage(), ResponseStatus.FAILURE);
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

}
