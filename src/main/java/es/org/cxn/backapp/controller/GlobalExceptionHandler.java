/**
 * The MIT License (MIT)
 *
 * <p>Copyright (c) 2020 the original author or authors.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.controller;

import es.org.cxn.backapp.response.DefaultResponse;
import es.org.cxn.backapp.response.Response;
import es.org.cxn.backapp.response.ResponseStatus;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
   * @param ex      the exception.
   * @param request the request.
   * @return Response entity.
   */
  @ExceptionHandler({ RuntimeException.class })
  public final ResponseEntity<Object>
        handleExceptionDefault(final Exception ex, final WebRequest request) {
    final HttpHeaders headers;
    final HttpStatus status;

    status = HttpStatus.INTERNAL_SERVER_ERROR;
    headers = new HttpHeaders();
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  /**
   * Response entity.
   */
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
        final Exception ex, final Object body, final HttpHeaders headers,
        final HttpStatusCode status, final WebRequest request
  ) {
    final Response<String> response;
    final String message;

    if (ex.getMessage() == null) {
      message = "";
    } else {
      message = ex.getMessage();
    }

    response = new DefaultResponse<>(message, ResponseStatus.FAILURE);

    return super.handleExceptionInternal(
          ex, response, headers, status, request
    );
  }

  /**
   * Response entity argument no valid.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException ex, final HttpHeaders headers,
        final HttpStatusCode status, final WebRequest request
  ) {
    final Iterable<String> errors;
    final Response<Iterable<String>> response;

    errors = ex.getBindingResult().getFieldErrors().stream()
          .map(x -> x.getDefaultMessage()).collect(Collectors.toList());

    response = new DefaultResponse<>(errors, ResponseStatus.WARNING);

    return super.handleExceptionInternal(
          ex, response, headers, status, request
    );
  }

}
