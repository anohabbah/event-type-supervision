package dev.abbah.supervision.eventtype.application.port.in;

import dev.abbah.supervision.eventtype.domain.EventType;
import reactor.core.publisher.Mono;

/**
 * Input port for creating a new event type.
 */
public interface CreateEventTypeUseCase {
    /**
     * Creates a new event type.
     *
     * @param eventType The event type to create
     * @return The created event type
     */
    Mono<EventType> createEventType(EventType eventType);
}
