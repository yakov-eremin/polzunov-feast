package com.example.polzunovfeastserver.place;

import com.example.polzunovfeastserver.place.entity.PlaceEntity;
import org.openapitools.model.Place;

public class PlaceMapper {
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
