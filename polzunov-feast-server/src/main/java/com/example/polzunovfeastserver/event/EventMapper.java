package com.example.polzunovfeastserver.event;

import com.example.polzunovfeastserver.category.CategoryEntity;
import com.example.polzunovfeastserver.image.ImageEntity;
import com.example.polzunovfeastserver.place.PlaceEntity;
import org.openapitools.model.Category;
import org.openapitools.model.Event;
import org.openapitools.model.EventWithPlaceResponse;
import org.openapitools.model.Place;

import java.util.Set;

public final class EventMapper {
    private EventMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static EventWithPlaceResponse toEventWithPlaceResponse(EventEntity eventEntity,
                                                                  Place place,
                                                                  Set<Category> categories,
                                                                  Set<String> imageUrls) {
        return new EventWithPlaceResponse(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getDescription(),
                eventEntity.getStartTime(),
                eventEntity.getEndTime(),
                place,
                eventEntity.isCanceled(),
                categories,
                eventEntity.getAgeLimit(),
                imageUrls
        );
    }

    public static EventEntity toEventEntity(Event event,
                                            PlaceEntity placeEntity,
                                            Set<CategoryEntity> categories,
                                            ImageEntity mainImageUrl,
                                            Set<ImageEntity> imageUrls) {
        return new EventEntity(
                event.getId(),
                event.getName().trim(),
                event.getDescription().trim(),
                event.getStartTime(),
                event.getEndTime(),
                placeEntity,
                event.getCanceled(),
                categories,
                event.getAgeLimit(),
                mainImageUrl,
                imageUrls
        );
    }
}
