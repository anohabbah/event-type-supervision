package dev.abbah.supervision.eventtype.application.port.in;

import dev.abbah.supervision.eventtype.domain.EventType;
import reactor.core.publisher.Mono;

/**
 * Input port for updating an existing event type.
 */
public interface UpdateEventTypeUseCase {
    /**
     * Updates an existing event type.
     *
     * @param id The ID of the event type to update
     * @param eventType The updated event type data
     * @return The updated event type
     */
    Mono<EventType> updateEventType(String id, EventType eventType);
}
