package dev.abbah.supervision.eventtype.adapter.in.web.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceNotFoundExceptionTest {

    @Test
    void constructor_shouldCreateExceptionWithMessage() {
        // Given
        String message = "Resource not found";
        
        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        // Then
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_shouldCreateExceptionWithMessageAndCause() {
        // Given
        String message = "Resource not found";
        Throwable cause = new RuntimeException("Original cause");
        
        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);
        
        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void forResource_shouldCreateExceptionWithFormattedMessage() {
        // Given
        String resourceType = "EventType";
        String id = "123";
        
        // When
        ResourceNotFoundException exception = ResourceNotFoundException.forResource(resourceType, id);
        
        // Then
        assertNotNull(exception);
        assertEquals("EventType with ID '123' not found", exception.getMessage());
    }
}
