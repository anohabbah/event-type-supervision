package dev.abbah.supervision.eventtype.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response DTO for event type operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeResponse {
    private String id;
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
