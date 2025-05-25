package dev.abbah.supervision.eventtype.adapter.out.persistence;

import dev.abbah.supervision.eventtype.adapter.out.persistence.mapper.EventTypePersistenceMapper;
import dev.abbah.supervision.eventtype.domain.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventTypePersistenceAdapterTest {

    @Mock
    private EventTypeMongoRepository repository;

    @Mock
    private EventTypePersistenceMapper mapper;

    @Mock
    private org.springframework.data.mongodb.core.ReactiveMongoTemplate mongoTemplate;

    @InjectMocks
    private EventTypePersistenceAdapter adapter;

    private EventType eventType;
    private EventTypeEntity eventTypeEntity;
    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        eventType = EventType.builder()
                .id("1")
                .name("Test Event")
                .description("Test Description")
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        eventTypeEntity = EventTypeEntity.builder()
                .id("1")
                .name("Test Event")
                .description("Test Description")
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    void save_shouldSaveEventType() {
        // Given
        when(mapper.toEntity(eventType)).thenReturn(eventTypeEntity);
        when(repository.save(eventTypeEntity)).thenReturn(Mono.just(eventTypeEntity));
        when(mapper.toDomain(eventTypeEntity)).thenReturn(eventType);

        // When
        Mono<EventType> result = adapter.save(eventType);

        // Then
        StepVerifier.create(result)
                .expectNext(eventType)
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEventType() {
        // Given
        when(repository.findById("1")).thenReturn(Mono.just(eventTypeEntity));
        when(mapper.toDomain(eventTypeEntity)).thenReturn(eventType);

        // When
        Mono<EventType> result = adapter.findById("1");

        // Then
        StepVerifier.create(result)
                .expectNext(eventType)
                .verifyComplete();
    }

    @Test
    void deleteById_shouldDeleteEventType() {
        // Given
        when(repository.deleteById("1")).thenReturn(Mono.empty());

        // When
        Mono<Void> result = adapter.deleteById("1");

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findAll_shouldReturnAllEventTypes() {
        // Given
        List<EventTypeEntity> entities = List.of(
                eventTypeEntity,
                EventTypeEntity.builder()
                        .id("2")
                        .name("Another Event")
                        .description("Another Description")
                        .active(true)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        // Mock the mongoTemplate aggregation operation
        when(mongoTemplate.aggregate(any(), any(Class.class))).thenReturn(Flux.fromIterable(entities));
        when(mapper.toDomain(any(EventTypeEntity.class))).thenReturn(eventType);

        // When
        Flux<EventType> result = adapter.findAll(pageable);

        // Then
        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void count_shouldReturnTotalCount() {
        // Given
        when(repository.count()).thenReturn(Mono.just(2L));

        // When
        Mono<Long> result = adapter.count();

        // Then
        StepVerifier.create(result)
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void search_shouldReturnMatchingEventTypes() {
        // Given
        String query = "test";
        List<EventTypeEntity> entities = List.of(eventTypeEntity);

        // Mock the mongoTemplate aggregation operation
        when(mongoTemplate.aggregate(any(), any(Class.class))).thenReturn(Flux.fromIterable(entities));
        when(mapper.toDomain(eventTypeEntity)).thenReturn(eventType);

        // When
        Flux<EventType> result = adapter.search(query, pageable);

        // Then
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void countByQuery_shouldReturnMatchCount() {
        // Given
        String query = "test";
        when(mongoTemplate.count(any(), any(Class.class))).thenReturn(Mono.just(1L));

        // When
        Mono<Long> result = adapter.countByQuery(query);

        // Then
        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }
}
