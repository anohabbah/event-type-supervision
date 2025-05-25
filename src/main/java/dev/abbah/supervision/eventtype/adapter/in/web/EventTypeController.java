package dev.abbah.supervision.eventtype.adapter.in.web;

import dev.abbah.supervision.eventtype.adapter.in.web.dto.EventTypeRequest;
import dev.abbah.supervision.eventtype.adapter.in.web.dto.EventTypeResponse;
import dev.abbah.supervision.eventtype.adapter.in.web.dto.PageResponse;
import dev.abbah.supervision.eventtype.adapter.in.web.mapper.EventTypeWebMapper;
import dev.abbah.supervision.eventtype.application.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * REST controller for event type operations.
 */
@RestController
@RequestMapping("/api/v1/event-types")
@RequiredArgsConstructor
@Tag(name = "Event Types", description = "API for event type management")
public class EventTypeController {

    private final CreateEventTypeUseCase createEventTypeUseCase;
    private final GetEventTypeUseCase getEventTypeUseCase;
    private final UpdateEventTypeUseCase updateEventTypeUseCase;
    private final DeleteEventTypeUseCase deleteEventTypeUseCase;
    private final ListEventTypesUseCase listEventTypesUseCase;
    private final SearchEventTypesUseCase searchEventTypesUseCase;
    private final EventTypeWebMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new event type", description = "Creates a new event type with the provided data")
    @ApiResponse(responseCode = "201", description = "Event type created successfully")
    public Mono<EventTypeResponse> createEventType(@Valid @RequestBody EventTypeRequest request) {
        return createEventTypeUseCase.createEventType(mapper.toDomain(request))
                .map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an event type by ID", description = "Retrieves an event type by its ID")
    @ApiResponse(responseCode = "200", description = "Event type found")
    @ApiResponse(responseCode = "404", description = "Event type not found")
    public Mono<ResponseEntity<EventTypeResponse>> getEventTypeById(
            @Parameter(description = "The ID of the event type to retrieve", required = true)
            @PathVariable String id) {
        return getEventTypeUseCase.getEventTypeById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an event type", description = "Updates an existing event type with the provided data")
    @ApiResponse(responseCode = "200", description = "Event type updated successfully")
    @ApiResponse(responseCode = "404", description = "Event type not found")
    public Mono<ResponseEntity<EventTypeResponse>> updateEventType(
            @Parameter(description = "The ID of the event type to update", required = true)
            @PathVariable String id,
            @Valid @RequestBody EventTypeRequest request) {
        return updateEventTypeUseCase.updateEventType(id, mapper.toDomain(request))
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an event type", description = "Deletes an event type by its ID")
    @ApiResponse(responseCode = "204", description = "Event type deleted successfully")
    public Mono<Void> deleteEventType(
            @Parameter(description = "The ID of the event type to delete", required = true)
            @PathVariable String id) {
        return deleteEventTypeUseCase.deleteEventType(id);
    }

    @GetMapping
    @Operation(summary = "List all event types", description = "Lists all event types with pagination")
    @ApiResponse(responseCode = "200", description = "Event types retrieved successfully")
    public Mono<PageResponse<EventTypeResponse>> listEventTypes(
            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        
        return listEventTypesUseCase.listEventTypes(pageRequest)
                .map(mapper::toResponse)
                .collectList()
                .zipWith(listEventTypesUseCase.countEventTypes())
                .map(tuple -> {
                    List<EventTypeResponse> content = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = calculateTotalPages(totalElements, size);
                    
                    PageResponse.PageMetadata metadata = PageResponse.PageMetadata.builder()
                            .pageNumber(page)
                            .pageSize(size)
                            .totalElements(totalElements)
                            .totalPages(totalPages)
                            .build();
                    
                    return PageResponse.<EventTypeResponse>builder()
                            .content(content)
                            .metadata(metadata)
                            .build();
                });
    }

    @GetMapping("/search")
    @Operation(summary = "Search event types", description = "Searches for event types by name or description with pagination")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public Mono<PageResponse<EventTypeResponse>> searchEventTypes(
            @Parameter(description = "Search query")
            @RequestParam String query,
            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        
        return searchEventTypesUseCase.searchEventTypes(query, pageRequest)
                .map(mapper::toResponse)
                .collectList()
                .zipWith(searchEventTypesUseCase.countSearchResults(query))
                .map(tuple -> {
                    List<EventTypeResponse> content = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = calculateTotalPages(totalElements, size);
                    
                    PageResponse.PageMetadata metadata = PageResponse.PageMetadata.builder()
                            .pageNumber(page)
                            .pageSize(size)
                            .totalElements(totalElements)
                            .totalPages(totalPages)
                            .build();
                    
                    return PageResponse.<EventTypeResponse>builder()
                            .content(content)
                            .metadata(metadata)
                            .build();
                });
    }
    
    private int calculateTotalPages(long totalElements, int pageSize) {
        return pageSize > 0 ? (int) Math.ceil((double) totalElements / (double) pageSize) : 0;
    }
}
