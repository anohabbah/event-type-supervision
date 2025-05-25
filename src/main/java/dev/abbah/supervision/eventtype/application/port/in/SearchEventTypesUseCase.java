package dev.abbah.supervision.eventtype.application.port.in;

import dev.abbah.supervision.eventtype.domain.EventType;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Input port for searching event types.
 */
public interface SearchEventTypesUseCase {
    /**
     * Searches for event types by name or description with pagination.
     *
     * @param query The search query
     * @param pageable Pagination information
     * @return A flux of matching event types
     */
    Flux<EventType> searchEventTypes(String query, Pageable pageable);
    
    /**
     * Counts the total number of event types matching the search query.
     *
     * @param query The search query
     * @return The total count of matching event types
     */
    Mono<Long> countSearchResults(String query);
}
