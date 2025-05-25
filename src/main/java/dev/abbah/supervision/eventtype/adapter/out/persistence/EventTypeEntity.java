package dev.abbah.supervision.eventtype.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * MongoDB document entity for event types.
 */
@Document(collection = "event_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeEntity {
    
    @Id
    private String id;
    
    @Indexed
    @TextIndexed
    private String name;
    
    @TextIndexed
    private String description;
    
    private boolean active;
    
    private Instant createdAt;
    
    private Instant updatedAt;
}
