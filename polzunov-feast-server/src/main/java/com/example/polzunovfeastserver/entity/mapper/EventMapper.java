package com.example.polzunovfeastserver.entity.mapper;

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

    public Event toEvent(EventEntity eventEntity) {
        Event event = new Event();
        event.setId(eventEntity.getId());
        event.setName(eventEntity.getName());
        event.setStartDateTime(eventEntity.getStartDateTime().toString());
        event.setEndDateTime(eventEntity.getEndDateTime().toString());
        event.setPlace(placeMapper.toPlace(eventEntity.getPlace()));
        return event;
    }
}
