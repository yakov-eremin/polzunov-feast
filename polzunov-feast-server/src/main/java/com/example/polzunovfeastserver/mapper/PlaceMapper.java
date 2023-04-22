package com.example.polzunovfeastserver.mapper;

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
}
