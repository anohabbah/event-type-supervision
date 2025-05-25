package dev.abbah.supervision.eventtype.adapter.in.web.exception;

/**
 * Exception thrown when there is an application-level error.
 */
public class ApplicationException extends RuntimeException {

    /**
     * Constructs a new application exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Constructs a new application exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
