package dev.abbah.supervision.eventtype.application.service;

import dev.abbah.supervision.eventtype.application.port.in.CreateEventTypeUseCase;
import dev.abbah.supervision.eventtype.application.port.in.DeleteEventTypeUseCase;
import dev.abbah.supervision.eventtype.application.port.in.GetEventTypeUseCase;
import dev.abbah.supervision.eventtype.application.port.in.ListEventTypesUseCase;
import dev.abbah.supervision.eventtype.application.port.in.SearchEventTypesUseCase;
import dev.abbah.supervision.eventtype.application.port.in.UpdateEventTypeUseCase;
import dev.abbah.supervision.eventtype.application.port.out.EventTypeRepository;
import dev.abbah.supervision.eventtype.domain.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;

/**
 * Service implementation for event type use cases.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventTypeService
    implements CreateEventTypeUseCase, GetEventTypeUseCase, UpdateEventTypeUseCase, DeleteEventTypeUseCase, ListEventTypesUseCase,
    SearchEventTypesUseCase {

  private final EventTypeRepository eventTypeRepository;

  @Override
  public Mono<EventType> createEventType(EventType eventType) {
    // Create a new EventType with the current timestamp
    EventType newEventType = EventType.builder()
                                      .id(eventType.id())
                                      .name(eventType.name())
                                      .description(eventType.description())
                                      .active(eventType.active())
                                      .createdAt(Instant.now())
                                      .updatedAt(Instant.now())
                                      .build();

    return eventTypeRepository.save(newEventType);
  }

  @Override
  public Mono<EventType> getEventTypeById(String id) {
    return eventTypeRepository.findById(id);
  }

  @Override
  public Mono<EventType> updateEventType(String id, EventType eventType) {
    return eventTypeRepository.findById(id)
                              .flatMap(existingEventType -> {
                                // Create updated EventType
                                EventType updatedEventType = EventType.builder()
                                                                      .id(existingEventType.id())
                                                                      .name(eventType.name())
                                                                      .description(eventType.description())
                                                                      .active(eventType.active())
                                                                      .createdAt(existingEventType.createdAt())
                                                                      .updatedAt(Instant.now())
                                                                      .build();

                                return eventTypeRepository.save(updatedEventType);
                              });
  }

  @Override
  public Mono<Void> deleteEventType(String id) {
    return eventTypeRepository.deleteById(id);
  }

  @Override
  public Flux<EventType> listEventTypes(Pageable pageable) {
    return eventTypeRepository.findAll(pageable);
  }

  @Override
  public Mono<Long> countEventTypes() {
    return eventTypeRepository.count();
  }

  @Override
  public Flux<EventType> searchEventTypes(String query, Pageable pageable) {
    return eventTypeRepository.search(query, pageable);
  }

  @Override
  public Mono<Long> countSearchResults(String query) {
    return eventTypeRepository.countByQuery(query);
  }
}
