package dev.abbah.supervision.eventtype.adapter.out.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for event types.
 */
@Repository
public interface EventTypeMongoRepository extends ReactiveMongoRepository<EventTypeEntity, String> {
    // Basic CRUD operations are inherited from ReactiveMongoRepository
    // Custom operations are now implemented using ReactiveMongoTemplate with Aggregation API
}
