package dev.abbah.supervision.eventtype.adapter.in.web.mapper;

import dev.abbah.supervision.eventtype.adapter.in.web.dto.EventTypeRequest;
import dev.abbah.supervision.eventtype.adapter.in.web.dto.EventTypeResponse;
import dev.abbah.supervision.eventtype.domain.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;

/**
 * Mapper for converting between domain EventType and web DTOs.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = Instant.class
)
public interface EventTypeWebMapper {

    /**
     * Converts a request DTO to a domain EventType.
     * Uses the builder pattern for the record.
     *
     * @param request The request DTO
     * @return The domain event type
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    EventType toDomain(EventTypeRequest request);

    /**
     * Converts a domain EventType to a response DTO.
     *
     * @param eventType The domain event type
     * @return The response DTO
     */
    EventTypeResponse toResponse(EventType eventType);
}
