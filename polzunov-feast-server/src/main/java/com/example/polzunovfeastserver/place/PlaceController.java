package com.example.polzunovfeastserver.place;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.PlaceApi;
import org.openapitools.model.Place;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PlaceController implements PlaceApi {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @Override
    public ResponseEntity<Place> addPlace(Place place) {
        Place newPlace = placeService.addPlace(place);
        log.info("Place with id={} added", newPlace.getId());
        return ResponseEntity.ok(newPlace);
    }

    @Override
    public ResponseEntity<Place> updatePlaceById(Place place) {
        Place updatedPlace = placeService.updatePlaceById(place);
        log.info("Place with id={} updated", updatedPlace.getId());
        return ResponseEntity.ok(updatedPlace);
    }

    @Override
    public ResponseEntity<List<Place>> getAllPlaces(Integer page, Integer size) {
        List<Place> places = placeService.getAllPlaces(page, size);
        log.info("Places page of size={} with index={} returned, actual size={}", size, page, places.size());
        return ResponseEntity.ok(places);
    }

    @Override
    public ResponseEntity<Place> getPlaceById(Long id) {
        Place place = placeService.getPlaceById(id);
        log.info("Place with id={} returned", place.getId());
        return ResponseEntity.ok(place);
    }

    @Override
    public ResponseEntity<Void> deletePlaceById(Long id) {
        placeService.deletePlaceById(id);
        log.info("Place with id={} deleted", id);
        return ResponseEntity.ok().build();
    }
}
