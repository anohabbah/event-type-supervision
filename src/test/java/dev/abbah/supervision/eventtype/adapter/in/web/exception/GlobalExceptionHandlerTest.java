package dev.abbah.supervision.eventtype.adapter.in.web.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFoundException_shouldReturnProblemDetail() {
        // Given
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        // When
        ProblemDetail result = handler.handleResourceNotFoundException(ex);

        // Then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        assertEquals("Resource Not Found", result.getTitle());
        assertEquals("Resource not found", result.getDetail());
        assertEquals(URI.create("https://api.supervision.abbah.dev/errors/not-found"), result.getType());
    }

    @Test
    void handleValidationExceptions_shouldReturnProblemDetailWithErrors() {
        // Given
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError = new FieldError("object", "field", "error message");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        // When
        ProblemDetail result = handler.handleValidationExceptions(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("Validation Error", result.getTitle());
        assertEquals("Validation failed", result.getDetail());
        assertEquals(URI.create("https://api.supervision.abbah.dev/errors/validation"), result.getType());
        
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) result.getProperties().get("errors");
        assertNotNull(errors);
        assertEquals("error message", errors.get("field"));
    }

    @Test
    void handleBusinessException_shouldReturnProblemDetail() {
        // Given
        Map<String, Object> details = new HashMap<>();
        details.put("key", "value");
        BusinessException ex = new BusinessException("Business rule violated", details);

        // When
        ProblemDetail result = handler.handleBusinessException(ex);

        // Then
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getStatus());
        assertEquals("Business Rule Violation", result.getTitle());
        assertEquals("Business rule violated", result.getDetail());
        assertEquals(URI.create("https://api.supervision.abbah.dev/errors/business-rule"), result.getType());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resultDetails = (Map<String, Object>) result.getProperties().get("details");
        assertNotNull(resultDetails);
        assertEquals("value", resultDetails.get("key"));
    }

    @Test
    void handleApplicationException_shouldReturnProblemDetail() {
        // Given
        ApplicationException ex = new ApplicationException("Application error occurred");

        // When
        ProblemDetail result = handler.handleApplicationException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
        assertEquals("Application Error", result.getTitle());
        assertEquals("Application error occurred", result.getDetail());
        assertEquals(URI.create("https://api.supervision.abbah.dev/errors/application"), result.getType());
    }

    @Test
    void handleResponseStatusException_shouldReturnProblemDetail() {
        // Given
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");

        // When
        ProblemDetail result = handler.handleResponseStatusException(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("Request Error", result.getTitle());
        assertEquals("Bad request", result.getDetail());
        assertEquals(URI.create("https://api.supervision.abbah.dev/errors/request"), result.getType());
    }

    @Test
    void handleGenericException_shouldReturnProblemDetail() {
        // Given
        Exception ex = new Exception("Generic error");

        // When
        ProblemDetail result = handler.handleGenericException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
        assertEquals("Server Error", result.getTitle());
        assertEquals("An unexpected error occurred", result.getDetail());
        assertEquals(URI.create("https://api.supervision.abbah.dev/errors/server"), result.getType());
    }
}
