package dev.abbah.supervision.eventtype.application.port.in;

import reactor.core.publisher.Mono;

/**
 * Input port for deleting an event type.
 */
public interface DeleteEventTypeUseCase {
    /**
     * Deletes an event type by its ID.
     *
     * @param id The ID of the event type to delete
     * @return A Mono that completes when the deletion is done
     */
    Mono<Void> deleteEventType(String id);
}
