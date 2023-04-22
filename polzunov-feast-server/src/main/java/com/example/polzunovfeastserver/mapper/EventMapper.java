package com.example.polzunovfeastserver.mapper;

import com.example.polzunovfeastserver.entity.EventEntity;
import org.openapitools.model.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {
    private final PlaceMapper placeMapper;

    public EventMapper(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    public EventEntity toEventEntity(Event event) {
        return new EventEntity(
                event.getId(),
                event.getName(),
                LocalDateTime.parse(event.getStartDateTime()),
                LocalDateTime.parse(event.getEndDateTime()),
                placeMapper.toPlaceEntity(event.getPlace()));
    }
}
