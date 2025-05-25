package dev.abbah.supervision.eventtype.adapter.out.persistence.mapper;

import dev.abbah.supervision.eventtype.adapter.out.persistence.EventTypeEntity;
import dev.abbah.supervision.eventtype.domain.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import dev.abbah.supervision.eventtype.adapter.out.persistence.EventTypeEntity;
import dev.abbah.supervision.eventtype.domain.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import dev.abbah.supervision.eventtype.adapter.out.persistence.EventTypeEntity;
import dev.abbah.supervision.eventtype.domain.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import dev.abbah.supervision.eventtype.adapter.out.persistence.EventTypeEntity;
import dev.abbah.supervision.eventtype.domain.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between domain EventType and persistence EventTypeEntity.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventTypePersistenceMapper {

    /**
     * Converts a domain EventType to an EventTypeEntity.
     *
     * @param eventType The domain event type
     * @return The persistence entity
     */
    EventTypeEntity toEntity(EventType eventType);

    /**
     * Converts an EventTypeEntity to a domain EventType.
     *
     * @param entity The persistence entity
     * @return The domain event type
     */
    EventType toDomain(EventTypeEntity entity);
}
