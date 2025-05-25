package dev.abbah.supervision.eventtype.adapter.in.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for converting exceptions to RFC 7807 Problem Details.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles resource not found exceptions.
   *
   * @param ex The exception
   * @return A ProblemDetail response
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setType(URI.create("https://api.supervision.abbah.dev/errors/not-found"));
    return problemDetail;
  }

  /**
   * Handles validation exceptions.
   *
   * @param ex The exception
   * @return A ProblemDetail response
   */
  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetail handleValidationExceptions(WebExchangeBindException ex) {
    Map<String, String> validationErrors = new HashMap<>();
    ex.getBindingResult()
      .getAllErrors()
      .forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        validationErrors.put(fieldName, errorMessage);
      });

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
    problemDetail.setTitle("Validation Error");
    problemDetail.setType(URI.create("https://api.supervision.abbah.dev/errors/validation"));
    problemDetail.setProperty("errors", validationErrors);

    return problemDetail;
  }

  /**
   * Handles business logic exceptions.
   *
   * @param ex The exception
   * @return A ProblemDetail response
   */
  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ProblemDetail handleBusinessException(BusinessException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    problemDetail.setTitle("Business Rule Violation");
    problemDetail.setType(URI.create("https://api.supervision.abbah.dev/errors/business-rule"));

    if (ex.getDetails() != null && !ex.getDetails()
                                      .isEmpty()) {
      problemDetail.setProperty("details", ex.getDetails());
    }

    return problemDetail;
  }

  /**
   * Handles general application exceptions.
   *
   * @param ex The exception
   * @return A ProblemDetail response
   */
  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ProblemDetail handleApplicationException(ApplicationException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    problemDetail.setTitle("Application Error");
    problemDetail.setType(URI.create("https://api.supervision.abbah.dev/errors/application"));
    return problemDetail;
  }

  /**
   * Handles response status exceptions.
   *
   * @param ex The exception
   * @return A ProblemDetail response
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatusException(ResponseStatusException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason() != null ? ex.getReason() : "Unexpected error");
    problemDetail.setTitle("Request Error");
    problemDetail.setType(URI.create("https://api.supervision.abbah.dev/errors/request"));
    return problemDetail;
  }

  /**
   * Fallback handler for all other exceptions.
   *
   * @param ex The exception
   * @return A ProblemDetail response
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ProblemDetail handleGenericException(Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    problemDetail.setTitle("Server Error");
    problemDetail.setType(URI.create("https://api.supervision.abbah.dev/errors/server"));
    return problemDetail;
  }
}
