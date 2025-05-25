package dev.abbah.supervision.eventtype.adapter.in.web.exception;

import lombok.Getter;

import java.util.Map;

/**
 * Exception thrown when a business rule is violated.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Map<String, Object> details; // NOSONAR

    /**
     * Constructs a new business exception with the specified detail message.
     *
     * @param message the detail message
     */
    public BusinessException(String message) {
        super(message);
        this.details = null;
    }

    /**
     * Constructs a new business exception with the specified detail message and additional details.
     *
     * @param message the detail message
     * @param details additional details about the exception
     */
    public BusinessException(String message, Map<String, Object> details) {
        super(message);
        this.details = details;
    }

    /**
     * Constructs a new business exception with the specified detail message, cause, and additional details.
     *
     * @param message the detail message
     * @param cause the cause
     * @param details additional details about the exception
     */
    public BusinessException(String message, Throwable cause, Map<String, Object> details) {
        super(message, cause);
        this.details = details;
    }
}
