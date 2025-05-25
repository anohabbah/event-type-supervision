package dev.abbah.supervision.eventtype.application.service;

import dev.abbah.supervision.eventtype.application.port.out.EventTypeRepository;
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
class EventTypeServiceTest {

  private final Pageable pageable = PageRequest.of(0, 10);
  @Mock
  private EventTypeRepository repository;
  @InjectMocks
  private EventTypeService service;
  private EventType eventType;

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
  }

  @Test
  void createEventType_shouldCreateNewEventType() {
    // Given
    when(repository.save(any(EventType.class))).thenReturn(Mono.just(eventType));

    // When
    Mono<EventType> result = service.createEventType(eventType);

    // Then
    StepVerifier.create(result)
                .expectNextMatches(created -> created.id()
                                                     .equals("1") && created.name()
                                                                            .equals("Test Event") && created.description()
                                                                                                            .equals(
                                                                                                                "Test Description") && created.active())
                .verifyComplete();
  }

  @Test
  void getEventTypeById_shouldReturnEventType() {
    // Given
    when(repository.findById("1")).thenReturn(Mono.just(eventType));

    // When
    Mono<EventType> result = service.getEventTypeById("1");

    // Then
    StepVerifier.create(result)
                .expectNext(eventType)
                .verifyComplete();
  }

  @Test
  void getEventTypeById_shouldReturnEmptyWhenNotFound() {
    // Given
    when(repository.findById("nonexistent")).thenReturn(Mono.empty());

    // When
    Mono<EventType> result = service.getEventTypeById("nonexistent");

    // Then
    StepVerifier.create(result)
                .verifyComplete();
  }

  @Test
  void updateEventType_shouldUpdateExistingEventType() {
    // Given
    EventType updatedEventType = EventType.builder()
                                          .id("1")
                                          .name("Updated Event")
                                          .description("Updated Description")
                                          .active(false)
                                          .build();

    when(repository.findById("1")).thenReturn(Mono.just(eventType));
    when(repository.save(any(EventType.class))).thenAnswer(invocation -> {
      EventType saved = invocation.getArgument(0);
      return Mono.just(saved);
    });

    // When
    Mono<EventType> result = service.updateEventType("1", updatedEventType);

    // Then
    StepVerifier.create(result)
                .expectNextMatches(updated -> updated.id()
                                                     .equals("1") && updated.name()
                                                                            .equals("Updated Event") && updated.description()
                                                                                                               .equals(
                                                                                                                   "Updated Description") && !updated.active())
                .verifyComplete();
  }

  @Test
  void deleteEventType_shouldDeleteEventType() {
    // Given
    when(repository.deleteById("1")).thenReturn(Mono.empty());

    // When
    Mono<Void> result = service.deleteEventType("1");

    // Then
    StepVerifier.create(result)
                .verifyComplete();
  }

  @Test
  void listEventTypes_shouldReturnAllEventTypes() {
    // Given
    List<EventType> eventTypes = List.of(eventType, EventType.builder()
                                                             .id("2")
                                                             .name("Another Event")
                                                             .description("Another Description")
                                                             .active(true)
                                                             .createdAt(Instant.now())
                                                             .updatedAt(Instant.now())
                                                             .build());

    when(repository.findAll(pageable)).thenReturn(Flux.fromIterable(eventTypes));

    // When
    Flux<EventType> result = service.listEventTypes(pageable);

    // Then
    StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
  }

  @Test
  void countEventTypes_shouldReturnTotalCount() {
    // Given
    when(repository.count()).thenReturn(Mono.just(2L));

    // When
    Mono<Long> result = service.countEventTypes();

    // Then
    StepVerifier.create(result)
                .expectNext(2L)
                .verifyComplete();
  }

  @Test
  void searchEventTypes_shouldReturnMatchingEventTypes() {
    // Given
    String query = "test";
    List<EventType> eventTypes = List.of(eventType);

    when(repository.search(query, pageable)).thenReturn(Flux.fromIterable(eventTypes));

    // When
    Flux<EventType> result = service.searchEventTypes(query, pageable);

    // Then
    StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
  }

  @Test
  void countSearchResults_shouldReturnMatchCount() {
    // Given
    String query = "test";
    when(repository.countByQuery(query)).thenReturn(Mono.just(1L));

    // When
    Mono<Long> result = service.countSearchResults(query);

    // Then
    StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
  }
}
