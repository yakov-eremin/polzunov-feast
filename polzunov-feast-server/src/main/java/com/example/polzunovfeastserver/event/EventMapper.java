package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.event.entity.EventEntity;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.openapitools.model.Place;

public class EventMapper {
    public static Event toEvent(EventEntity eventEntity) {
        Event event = new Event();
        event.setId(eventEntity.getId());
        event.setName(eventEntity.getName());
        event.setDescription(eventEntity.getDescription());
        event.setStartTime(eventEntity.getStartTime());
        event.setEndTime(eventEntity.getEndTime());
        event.setPlaceId(eventEntity.getPlace().getId());
        return event;
    }

    public static EventWithPlaceResponse toEventWithPlaceResponse(EventEntity eventEntity, Place place) {
        EventWithPlaceResponse event = new EventWithPlaceResponse();
        event.setId(eventEntity.getId());
        event.setName(eventEntity.getName());
        event.setDescription(eventEntity.getDescription());
        event.setStartTime(eventEntity.getStartTime());
        event.setEndTime(eventEntity.getEndTime());
        event.setPlace(place);
        return event;
    }

    public static EventEntity toEventEntity(Event event, PlaceEntity placeEntity) {
        return new EventEntity(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                placeEntity
        );
    }
}
