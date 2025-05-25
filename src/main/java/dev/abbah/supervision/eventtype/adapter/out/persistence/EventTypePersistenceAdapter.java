package dev.abbah.supervision.eventtype.adapter.out.persistence;

import dev.abbah.supervision.eventtype.adapter.out.persistence.mapper.EventTypePersistenceMapper;
import dev.abbah.supervision.eventtype.application.port.out.EventTypeRepository;
import dev.abbah.supervision.eventtype.domain.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistence adapter implementation for event type repository.
 */
@Component
@RequiredArgsConstructor
public class EventTypePersistenceAdapter implements EventTypeRepository {
    
    private final EventTypeMongoRepository repository;
    private final EventTypePersistenceMapper mapper;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<EventType> save(EventType eventType) {
        return Mono.just(eventType)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<EventType> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
    
    @Override
    public Flux<EventType> findAll(Pageable pageable) {
        // Create list of aggregation operations
        List<AggregationOperation> operations = new ArrayList<>();

        // Add sort if present
        if (pageable.getSort().isSorted()) {
            operations.add(Aggregation.sort(pageable.getSort()));
        } else {
            // Default sort by createdAt desc
            operations.add(Aggregation.sort(Sort.by(Sort.Direction.DESC, "createdAt")));
        }

        // Add pagination
        operations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        operations.add(Aggregation.limit(pageable.getPageSize()));

        // Create aggregation
        TypedAggregation<EventTypeEntity> aggregation = Aggregation.newAggregation(
                EventTypeEntity.class,
                operations
        );

        // Execute aggregation
        return mongoTemplate.aggregate(aggregation, EventTypeEntity.class)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Long> count() {
        return repository.count();
    }
    
    @Override
    public Flux<EventType> search(String query, Pageable pageable) {
        // Create text criteria for search
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(query);

        // Create a list of aggregation operations
        List<AggregationOperation> operations = new ArrayList<>();

        // Add text match operation
        operations.add(Aggregation.match(textCriteria));

        // Add sort if present
        if (pageable.getSort().isSorted()) {
            operations.add(Aggregation.sort(pageable.getSort()));
        } else {
            // Default sort by text score
            operations.add(Aggregation.sort(Sort.by("score").descending()));
        }

        // Add pagination
        operations.add(Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize()));
        operations.add(Aggregation.limit(pageable.getPageSize()));

        // Create aggregation
        TypedAggregation<EventTypeEntity> aggregation = Aggregation.newAggregation(
                EventTypeEntity.class,
                operations
        );

        // Execute aggregation
        return mongoTemplate.aggregate(aggregation, EventTypeEntity.class)
                .map(mapper::toDomain);
    }
    
    @Override
    public Mono<Long> countByQuery(String query) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(query);
        Query textQuery = TextQuery.queryText(textCriteria).limit(0);
        return mongoTemplate.count(textQuery, EventTypeEntity.class);
    }
}
