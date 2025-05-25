package dev.abbah.supervision.eventtype.application.port.out;

import dev.abbah.supervision.eventtype.domain.EventType;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Output port for event type repository operations.
 */
public interface EventTypeRepository {
    /**
     * Saves an event type.
     *
     * @param eventType The event type to save
     * @return The saved event type
     */
    Mono<EventType> save(EventType eventType);
    
    /**
     * Finds an event type by its ID.
     *
     * @param id The ID of the event type to find
     * @return The event type, or empty if not found
     */
    Mono<EventType> findById(String id);
    
    /**
     * Deletes an event type by its ID.
     *
     * @param id The ID of the event type to delete
     * @return A Mono that completes when the deletion is done
     */
    Mono<Void> deleteById(String id);
    
    /**
     * Finds all event types with pagination.
     *
     * @param pageable Pagination information
     * @return A flux of event types
     */
    Flux<EventType> findAll(Pageable pageable);
    
    /**
     * Counts the total number of event types.
     *
     * @return The total count
     */
    Mono<Long> count();
    
    /**
     * Searches for event types by name or description with pagination.
     *
     * @param query The search query
     * @param pageable Pagination information
     * @return A flux of matching event types
     */
    Flux<EventType> search(String query, Pageable pageable);
    
    /**
     * Counts the total number of event types matching the search query.
     *
     * @param query The search query
     * @return The total count of matching event types
     */
    Mono<Long> countByQuery(String query);
}
