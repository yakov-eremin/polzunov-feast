package com.example.polzunovfeastserver.entity.mapper;

import com.example.polzunovfeastserver.entity.PlaceEntity;
import org.openapitools.model.Place;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper {

    public PlaceEntity toPlaceEntity(Place place) {
        return new PlaceEntity(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude());
    }

    public Place toPlace(PlaceEntity placeEntity) {
        Place place = new Place();
        place.setId(placeEntity.getId());
        place.setName(placeEntity.getName());
        place.setAddress(placeEntity.getAddress());
        place.setLatitude(placeEntity.getLatitude());
        place.setLongitude(placeEntity.getLongitude());
        return place;
    }
}
