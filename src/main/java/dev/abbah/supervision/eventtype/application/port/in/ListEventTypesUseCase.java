package dev.abbah.supervision.eventtype.application.port.in;

import dev.abbah.supervision.eventtype.domain.EventType;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Input port for listing event types with pagination.
 */
public interface ListEventTypesUseCase {
    /**
     * Lists all event types with pagination.
     *
     * @param pageable Pagination information
     * @return A flux of event types
     */
    Flux<EventType> listEventTypes(Pageable pageable);
    
    /**
     * Counts the total number of event types.
     *
     * @return The total count of event types
     */
    Mono<Long> countEventTypes();
}
