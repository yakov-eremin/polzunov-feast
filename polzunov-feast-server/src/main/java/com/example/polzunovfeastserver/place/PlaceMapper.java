package com.example.polzunovfeastserver.place;

import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import org.openapitools.model.Place;

public final class PlaceMapper {
    private PlaceMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static Place toPlace(PlaceEntity placeEntity) {
        Place place = new Place();
        place.setId(placeEntity.getId());
        place.setName(placeEntity.getName());
        place.setAddress(placeEntity.getAddress());
        return place;
    }

    public static PlaceEntity toPlaceEntity(Place place) {
        return new PlaceEntity(
            place.getId(),
            place.getName(),
            place.getAddress()
        );
    }
}
