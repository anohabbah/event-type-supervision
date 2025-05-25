package dev.abbah.supervision.eventtype.application.port.in;

import dev.abbah.supervision.eventtype.domain.EventType;
import reactor.core.publisher.Mono;

/**
 * Input port for retrieving an event type.
 */
public interface GetEventTypeUseCase {
    /**
     * Retrieves an event type by its ID.
     *
     * @param id The ID of the event type to retrieve
     * @return The event type, or empty if not found
     */
    Mono<EventType> getEventTypeById(String id);
}
