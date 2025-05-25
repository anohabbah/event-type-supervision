package dev.abbah.supervision.eventtype.domain;

import lombok.Builder;

import java.time.Instant;

/**
 * Domain entity representing an event type.
 * Implemented as an immutable Java record with builder pattern support.
 */
@Builder(toBuilder = true)
public record EventType(
    String id,
    String name,
    String description,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {
}
