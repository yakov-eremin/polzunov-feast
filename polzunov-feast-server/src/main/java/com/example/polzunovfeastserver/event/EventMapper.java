package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.category.entity.CategoryEntity;
import com.example.polzunovfeastserver.event.entity.EventEntity;
import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import org.openapitools.model.Category;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.openapitools.model.Place;

import java.util.Set;

public final class EventMapper {
    private EventMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static EventWithPlaceResponse toEventWithPlaceResponse(EventEntity eventEntity, Place place, Set<Category> categories) {
        EventWithPlaceResponse event = new EventWithPlaceResponse();
        event.setId(eventEntity.getId());
        event.setName(eventEntity.getName());
        event.setDescription(eventEntity.getDescription());
        event.setStartTime(eventEntity.getStartTime());
        event.setEndTime(eventEntity.getEndTime());
        event.setPlace(place);
        event.setCanceled(eventEntity.isCanceled());
        event.setCategories(categories);
        event.setAgeLimit(eventEntity.getAgeLimit());
        return event;
    }

    public static EventEntity toEventEntity(Event event, PlaceEntity placeEntity, Set<CategoryEntity> categories) {
        return new EventEntity(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                placeEntity,
                event.getCanceled(),
                categories,
                event.getAgeLimit()
        );
    }
}
