package dev.abbah.supervision.eventtype.adapter.in.web.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new resource not found exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new resource not found exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a resource not found exception for a specific resource with the given ID.
     *
     * @param resourceType the type of resource
     * @param id the ID of the resource
     * @return a new resource didn't find exception
     */
    public static ResourceNotFoundException forResource(String resourceType, String id) {
        return new ResourceNotFoundException(resourceType + " with ID '" + id + "' not found");
    }
}
