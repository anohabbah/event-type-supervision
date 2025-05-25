package dev.abbah.supervision.eventtype.adapter.in.web;

import dev.abbah.supervision.eventtype.adapter.in.web.dto.EventTypeRequest;
import dev.abbah.supervision.eventtype.adapter.in.web.dto.EventTypeResponse;
import dev.abbah.supervision.eventtype.adapter.in.web.exception.GlobalExceptionHandler;
import dev.abbah.supervision.eventtype.adapter.in.web.mapper.EventTypeWebMapper;
import dev.abbah.supervision.eventtype.application.port.in.*;
import dev.abbah.supervision.eventtype.domain.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(EventTypeController.class)
@Import(GlobalExceptionHandler.class)
class EventTypeControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private CreateEventTypeUseCase createEventTypeUseCase;

    @MockitoBean
    private GetEventTypeUseCase getEventTypeUseCase;

    @MockitoBean
    private UpdateEventTypeUseCase updateEventTypeUseCase;

    @MockitoBean
    private DeleteEventTypeUseCase deleteEventTypeUseCase;

    @MockitoBean
    private ListEventTypesUseCase listEventTypesUseCase;

    @MockitoBean
    private SearchEventTypesUseCase searchEventTypesUseCase;

    @MockitoBean
    private EventTypeWebMapper mapper;

    @Test
    void createEventType_shouldCreateNewEventType() {
        // Given
        EventTypeRequest request = new EventTypeRequest("Test Event", "Test Description", true);
        EventType domain = new EventType("1", "Test Event", "Test Description", true, Instant.now(), Instant.now());
        EventTypeResponse response = new EventTypeResponse("1", "Test Event", "Test Description", true, Instant.now(), Instant.now());

        when(mapper.toDomain(any(EventTypeRequest.class))).thenReturn(domain);
        when(createEventTypeUseCase.createEventType(any(EventType.class))).thenReturn(Mono.just(domain));
        when(mapper.toResponse(any(EventType.class))).thenReturn(response);

        // When & Then
        webClient.post()
                .uri("/api/v1/event-types")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EventTypeResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getEventTypeById_shouldReturnEventType() {
        // Given
        String id = "1";
        EventType domain = new EventType(id, "Test Event", "Test Description", true, Instant.now(), Instant.now());
        EventTypeResponse response = new EventTypeResponse(id, "Test Event", "Test Description", true, Instant.now(), Instant.now());

        when(getEventTypeUseCase.getEventTypeById(id)).thenReturn(Mono.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        // When & Then
        webClient.get()
                .uri("/api/v1/event-types/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventTypeResponse.class)
                .isEqualTo(response);
    }

    @Test
    void getEventTypeById_shouldReturn404WhenNotFound() {
        // Given
        String id = "nonexistent";
        when(getEventTypeUseCase.getEventTypeById(id)).thenReturn(Mono.empty());

        // When & Then
        webClient.get()
                .uri("/api/v1/event-types/{id}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.type").isEqualTo("https://api.supervision.abbah.dev/errors/not-found")
                .jsonPath("$.title").isEqualTo("Resource Not Found")
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.detail").exists();
    }

    @Test
    void updateEventType_shouldUpdateExistingEventType() {
        // Given
        String id = "1";
        EventTypeRequest request = new EventTypeRequest("Updated Event", "Updated Description", false);
        EventType domain = new EventType(id, "Updated Event", "Updated Description", false, Instant.now(), Instant.now());
        EventTypeResponse response = new EventTypeResponse(id, "Updated Event", "Updated Description", false, Instant.now(), Instant.now());

        when(mapper.toDomain(request)).thenReturn(domain);
        when(updateEventTypeUseCase.updateEventType(anyString(), any(EventType.class))).thenReturn(Mono.just(domain));
        when(mapper.toResponse(domain)).thenReturn(response);

        // When & Then
        webClient.put()
                .uri("/api/v1/event-types/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventTypeResponse.class)
                .isEqualTo(response);
    }

    @Test
    void updateEventType_shouldReturn404WhenNotFound() {
        // Given
        String id = "nonexistent";
        EventTypeRequest request = new EventTypeRequest("Updated Event", "Updated Description", false);
        EventType domain = new EventType(id, "Updated Event", "Updated Description", false, Instant.now(), Instant.now());

        when(mapper.toDomain(request)).thenReturn(domain);
        when(updateEventTypeUseCase.updateEventType(anyString(), any(EventType.class))).thenReturn(Mono.empty());

        // When & Then
        webClient.put()
                .uri("/api/v1/event-types/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.type").isEqualTo("https://api.supervision.abbah.dev/errors/not-found")
                .jsonPath("$.title").isEqualTo("Resource Not Found")
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    void deleteEventType_shouldDeleteEventType() {
        // Given
        String id = "1";
        EventType domain = new EventType(id, "Test Event", "Test Description", true, Instant.now(), Instant.now());

        when(getEventTypeUseCase.getEventTypeById(id)).thenReturn(Mono.just(domain));
        when(deleteEventTypeUseCase.deleteEventType(id)).thenReturn(Mono.empty());

        // When & Then
        webClient.delete()
                .uri("/api/v1/event-types/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteEventType_shouldReturn404WhenNotFound() {
        // Given
        String id = "nonexistent";
        when(getEventTypeUseCase.getEventTypeById(id)).thenReturn(Mono.empty());

        // When & Then
        webClient.delete()
                .uri("/api/v1/event-types/{id}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.type").isEqualTo("https://api.supervision.abbah.dev/errors/not-found")
                .jsonPath("$.title").isEqualTo("Resource Not Found")
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    void listEventTypes_shouldReturnEventTypes() {
        // Given
        EventType eventType1 = new EventType("1", "Event 1", "Description 1", true, Instant.now(), Instant.now());
        EventType eventType2 = new EventType("2", "Event 2", "Description 2", true, Instant.now(), Instant.now());
        
        EventTypeResponse response1 = new EventTypeResponse("1", "Event 1", "Description 1", true, Instant.now(), Instant.now());
        EventTypeResponse response2 = new EventTypeResponse("2", "Event 2", "Description 2", true, Instant.now(), Instant.now());

        when(listEventTypesUseCase.listEventTypes(any(PageRequest.class))).thenReturn(Flux.just(eventType1, eventType2));
        when(listEventTypesUseCase.countEventTypes()).thenReturn(Mono.just(2L));
        when(mapper.toResponse(eventType1)).thenReturn(response1);
        when(mapper.toResponse(eventType2)).thenReturn(response2);

        // When & Then
        webClient.get()
                .uri("/api/v1/event-types?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content.length()").isEqualTo(2)
                .jsonPath("$.metadata.totalElements").isEqualTo(2)
                .jsonPath("$.metadata.pageNumber").isEqualTo(0)
                .jsonPath("$.metadata.pageSize").isEqualTo(10);
    }

    @Test
    void searchEventTypes_shouldReturnMatchingEventTypes() {
        // Given
        String query = "test";
        EventType eventType = new EventType("1", "Test Event", "Test Description", true, Instant.now(), Instant.now());
        EventTypeResponse response = new EventTypeResponse("1", "Test Event", "Test Description", true, Instant.now(), Instant.now());

        when(searchEventTypesUseCase.searchEventTypes(anyString(), any(PageRequest.class))).thenReturn(Flux.just(eventType));
        when(searchEventTypesUseCase.countSearchResults(query)).thenReturn(Mono.just(1L));
        when(mapper.toResponse(eventType)).thenReturn(response);

        // When & Then
        webClient.get()
                .uri("/api/v1/event-types/search?query={query}&page=0&size=10", query)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content.length()").isEqualTo(1)
                .jsonPath("$.metadata.totalElements").isEqualTo(1);
    }

    @Test
    void searchEventTypes_shouldReturnBadRequestWhenQueryIsEmpty() {
        // When & Then
        webClient.get()
                .uri("/api/v1/event-types/search?query=&page=0&size=10")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.type").isEqualTo("https://api.supervision.abbah.dev/errors/business-rule")
                .jsonPath("$.title").isEqualTo("Business Rule Violation")
                .jsonPath("$.status").isEqualTo(422);
    }
}
